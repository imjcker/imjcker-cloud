package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.plugin.queue.MessageHandler;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.service.AuthService;
import com.imjcker.manager.charge.service.CustomerRechargeService;
import com.imjcker.manager.charge.service.DatasourceChargeService;
import com.imjcker.manager.charge.service.KafkaService;
import com.imjcker.manager.charge.utils.RedisLuaOperateUtil;
import com.imjcker.manager.charge.vo.request.ReqAppkeyByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.RedisKeyUtil;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisUtil;
import com.lemon.common.vo.CustomerChargeMessageVo;
import com.lemon.common.vo.MessageModeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author WT
 * @Date 10:35 2020/2/24
 * @Version CustomerRechargeServiceImpl v1.0
 * @Desicrption
 */
@Service
public class CustomerRechargeServiceImpl implements CustomerRechargeService {

    @Autowired
    private CompanyAppsMapper companyAppsMapper;

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;

    @Autowired
    private BillingRulesMapper billingRulesMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private CompanyBalanceHistoryMapper companyBalanceHistoryMapper;

    @Autowired
    private CompanyAppsRechargeMapper companyAppsRechargeMapper;

    @Autowired
    private CompanyAppsVersionMapper companyAppsVersionMapper;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private DatasourceChargeService datasourceChargeService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerRechargeServiceImpl.class);

    @Override
    public PageInfo<CompanyAppsVo> queryCustomer(CompanyAppParam appParam) {

        PageHelper.startPage(appParam.getPageNum() == null ? 1 : appParam.getPageNum(),
                appParam.getPageSize() == null ? 10 : appParam.getPageSize());

        List<CompanyAppsVo> companyApps = companyAppsMapper.queryCustomer(appParam.getAppKey());
        String todayDate = DateUtil.getTodayDate();
        companyApps.forEach(app -> {

            String balance = RedisUtil.get(RedisKeyUtil
                    .getCustomerBalanceKeyByDate(app.getAppKey(),todayDate), String.class);
            if (StringUtils.isNotBlank(balance)) {
                app.setBalance(new BigDecimal(balance));
            }
            Long stock = RedisUtil.get(RedisKeyUtil
                    .getCustomerLimitKeyByDate(app.getAppKey(),todayDate), Long.class);
            if (stock != null)
                app.setStock(stock);
        });
        return new PageInfo<>(companyApps);
    }

    @Override
    @Transactional
    public void charge(CompanyAppsRecharge appRecharge) {
        String todayDate = DateUtil.getTodayDate();
        appRecharge.setCreateTime(System.currentTimeMillis());

        companyAppsRechargeMapper.insertSelective(appRecharge);

        // 更新redis余额
        CompanyAppsVo appVo = companyAppsMapper.findCustomerByAppKey(appRecharge.getAppKey());
        String customerBalanceKey = RedisKeyUtil.getCustomerBalanceKeyByDate(appVo.getAppKey(), todayDate);
        RedisLuaOperateUtil.upRedisBalance(customerBalanceKey, appVo.getBalance().toString(), appRecharge.getAmount().toString());
        // 发送kafka到flink
        CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                .appKey(appRecharge.getAppKey())
                .money(appRecharge.getAmount())
                .count(0)
                .isDeleteAuthCount(false)
                .isDeleteCustomerBalance(false)
                .isDeleteCustomerCount(false)
                .build();
        new MessageHandler() {
            @Override
            public void handle() {
                try {
                    String message = JSON.toJSONString(customerChargeMessageVo);
                    kafkaService.sendCustomerFlink(message);
                    logger.debug("客户充值发送kafka到flink: {}", message);
                } catch (Exception e) {
                    logger.error("客户充值发送kafka到flink失败: {}", e);
                }
            }
        }.putQueue();
    }

    @Override
    public PageInfo<CompanyAppsRecharge> queryChargeRecord(CompanyAppParam appParam) {

        PageHelper.startPage(appParam.getPageNum() == null ? 1 : appParam.getPageNum(),
                appParam.getPageSize() == null ? 10 : appParam.getPageSize());
        List<CompanyAppsRecharge> companyAppRecharges = companyAppsRechargeMapper.queryChargeRecord(appParam.getAppKey());

        return new PageInfo<>(companyAppRecharges);
    }

    /**
     * @Description : 新增客户appKey
     * @param app
     * @Return : void
     * @Date : 2020/3/25 9:27
     */
    @Override
    @Transactional
    public void addCustomer(CompanyApps app) {
        app.setStatusFlag(1);
        List<String> appKeys = new ArrayList<>();
        appKeys.add(app.getAppKey());
        List<CompanyApps> companyApps = companyAppsMapper.listByAppKeys(appKeys);
        if(companyApps!=null && companyApps.size()>0){
            throw new BusinessException("appKey重复");
        }
        long now = System.currentTimeMillis();
        app.setCreateTime(now);
        app.setUpdateTime(now);
        // 查询计费规则下的调用次数限制
        Long stock = null;
        if (StringUtils.isNotBlank(app.getStrategyUuid())) {
            BillingRules billingRules = billingRulesMapper.selectByBillingRulesUuid(app.getStrategyUuid());
            // 如果客户级别拥有计费规则并且是按照包时计费,则判断创建时余额是否足够一次性扣除套餐费用
            if (billingRules.getBillingType() == 1) {
                // 包时
                int i = app.getBalance().compareTo(app.getPrice());
                if (i >= 0) {
                    // 余额足够
                    BigDecimal subtract = app.getBalance().subtract(app.getPrice()).setScale(2, BigDecimal.ROUND_DOWN);
                    app.setBalance(subtract);
                } else {
                    // 不够扣除
                    throw new DataValidationException("新增客户的余额不足以购买所选择的包时计费套餐");
                }
                stock = billingRules.getBillingCycleLimit();
                app.setStock(stock);
            }
        } else {
            // 修改客户表的余量和价格
            app.setStock(null);
            app.setPrice(null);
        }
        companyAppsMapper.addCustomer(app);
        CompanyAppsVersion appsVersion = new CompanyAppsVersion();
        appsVersion.setAppKey(app.getAppKey());
        appsVersion.setStrategyUuid(app.getStrategyUuid());
        appsVersion.setCreateTime(now);
        appsVersion.setUpdateTime(now);
        appsVersion.setPrice(app.getPrice());
        companyAppsVersionMapper.insertSelective(appsVersion);
        // 设置余额缓存,加上当天日期
        String todayDate = DateUtil.getTodayDate();
        String customerBalance = RedisKeyUtil.getCustomerBalanceKeyByDate(app.getAppKey(), todayDate);
        RedisUtil.setToCaches(customerBalance,app.getBalance());
        // 设置客户信息缓存
        RedisUtil.setToCaches(RedisKeyUtil.getCustomerInfoKey(app.getAppKey()), app);
        // 初始化新增客户发送kafka到flink的消息
        CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                .appKey(app.getAppKey())
                .money(app.getBalance())
                .count(0)
                .isDeleteAuthCount(false)
                .isDeleteCustomerBalance(false)
                .isDeleteCustomerCount(false)
                .build();
        // 设置调用次数缓存
        if (null != stock) {
            String customerLimit = RedisKeyUtil.getCustomerLimitKeyByDate(app.getAppKey(), todayDate);
            RedisUtil.setToCaches(customerLimit, stock);
            customerChargeMessageVo.setCount(stock.intValue());
        }
        new MessageHandler() {
            @Override
            public void handle() {
                try {
                    String message = JSON.toJSONString(customerChargeMessageVo);
                    kafkaService.sendCustomerFlink(message);
                    logger.debug("新增客户发送消息到flink成功: {}", message);
                } catch (Exception e) {
                    logger.error("新增客户发送消息到flink失败: {}", e);
                }
            }
        }.putQueue();
    }

    /**
     * @Description : 修改客户
     * @param app
     * @Return : void
     * @Date : 2020/3/25 9:28
     */
    @Override
    @Transactional
    public void updateCustomer(CompanyApps app) {
        CompanyAppsVo companyAppsVo = companyAppsMapper.findCustomerByAppKey(app.getAppKey());
        if (companyAppsVo == null)
            return;
        long now = System.currentTimeMillis();
        LocalDateTime dateTime = Instant.ofEpochMilli(now).atOffset(ZoneOffset.ofHours(8)).toLocalDateTime();
        app.setUpdateTime(now);
        // 查询计费规则下的调用次数限制
        BillingRules billingRules = null;
        BillingRules apiBillingRules = null;
        String operatorBalanceKey = null;
        String operatorLimitKey = null;
        String initBalanceValue = null;
        String initLimitValue = null;
        String operatorBalanceValue = null;
        String operatorLimitValue = null;
        Long newEndTime = null;
        String todayDate = DateUtil.getTodayDate();
        List<String> delKeys = new ArrayList<>();
        List<CompanyAppsAuth> list = companyAppsAuthMapper.findAppKeyAndApiId(app.getAppKey(), null, null);
        CustomerChargeMessageVo customerChargeMessageVo = null;
        // 客户具有计费规则
        if (StringUtils.isNotBlank(app.getStrategyUuid())) {
            customerChargeMessageVo = CustomerChargeMessageVo.builder()
                    .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                    .appKey(app.getAppKey())
                    .money(BigDecimal.ZERO)
                    .count(0)
                    .isDeleteCustomerBalance(false)
                    .isDeleteCustomerCount(false)
                    .isDeleteAuthCount(false)
                    .build();
            billingRules = billingRulesMapper.selectByBillingRulesUuid(app.getStrategyUuid());
            // 判断修改前客户是否具有计费模式
            if (StringUtils.isNotBlank(companyAppsVo.getStrategyUuid())) {
                BillingRules oldBillingRules = billingRulesMapper.selectByBillingRulesUuid(companyAppsVo.getStrategyUuid());
                // 修改前按照时间计费,包时
                if (oldBillingRules.getBillingType() == 1) {
                    if (billingRules.getBillingType() == 1) {
                        // 修改后也是包时计费,直接扣费,并将合约时间更改,同步更新合约表,增加调用余量
                        operatorBalanceKey = RedisKeyUtil.getCustomerBalanceKeyByDate(app.getAppKey(), todayDate);
                        operatorBalanceValue = app.getPrice().toString();
                        initBalanceValue = companyAppsVo.getBalance().toString();
                        operatorLimitKey = RedisKeyUtil.getCustomerLimitKeyByDate(app.getAppKey(), todayDate);
                        operatorLimitValue = billingRules.getBillingCycleLimit().toString();
                        initLimitValue = oldBillingRules.getBillingCycleLimit().toString();
                        // 发送flink扣费,取价格的负数
                        customerChargeMessageVo.setMoney(app.getPrice().negate());
                        customerChargeMessageVo.setCount(billingRules.getBillingCycleLimit().intValue());
                        if (list != null && list.size() > 0) {
                            CompanyAppsAuth companyAppsAuth = list.get(0);
                            long startTime = companyAppsAuth.getStartTime();
                            long endTime = companyAppsAuth.getEndTime();
                            long remainTime = endTime - startTime;
                            // 修改合约时间
                            long l = calculateTime(dateTime, billingRules.getBillingCycle());
                            newEndTime = l + remainTime;
                        }
                    } else {
                        // 修改后为按条收费, 同步更新合约表,删除调用余量缓存
                        app.setStock(null);
                        delKeys.add(RedisKeyUtil.getCustomerLimitKeyByDate(app.getAppKey(), todayDate));
                        customerChargeMessageVo.setIsDeleteCustomerCount(true);
                    }
                } else {
                    // 修改前为按条收费
                    if (billingRules.getBillingType() == 1) {
                        // 修改后是包时计费,扣费,并将合约时间更改,同步更新合约表,增加调用余量
                        operatorBalanceKey = RedisKeyUtil.getCustomerBalanceKeyByDate(app.getAppKey(), todayDate);
                        operatorBalanceValue = app.getPrice().toString();
                        initBalanceValue = companyAppsVo.getBalance().toString();
                        newEndTime = calculateTime(dateTime, billingRules.getBillingCycle());
                        operatorLimitKey = RedisKeyUtil.getCustomerLimitKeyByDate(app.getAppKey(), todayDate);
                        operatorLimitValue = billingRules.getBillingCycleLimit().toString();
                        initLimitValue = "0";
                        customerChargeMessageVo.setMoney(app.getPrice().negate());
                        customerChargeMessageVo.setCount(billingRules.getBillingCycleLimit().intValue());
                    }
                }
            } else {
                // 修改前不是客户级别计费, 就是从接口级别统一到客户级别, 需要删除接口的调用余量
                // flink删除appKey下合约的调用余量
                customerChargeMessageVo.setIsDeleteAuthCount(true);
                if (billingRules.getBillingType() == 1) {
                    // 包时计费, 查询绑定的接口级别计费方式,如果是包时计费,将所有的接口级别的调用次数相加,再加上当前客户分配的次数
                    // 接口包时计费,需要删除接口的调用次数缓存
                    int remainLimit = 0;
                    if (null != list) {
                        for (CompanyAppsAuth companyAppsAuth : list) {
                            apiBillingRules = billingRulesMapper.selectByBillingRulesUuid(companyAppsAuth.getStrategyUuid());
                            if (apiBillingRules.getBillingType() == 1) {
                                Integer integer = RedisUtil.get(RedisKeyUtil.getAuthLimitKeyByDate(app.getAppKey(),
                                        companyAppsAuth.getApiId(), todayDate), Integer.class);
                                if (integer != null)
                                    remainLimit += integer;
                                else
                                    remainLimit += apiBillingRules.getBillingCycleLimit();
                                delKeys.add(RedisKeyUtil.getAuthLimitKeyByDate(app.getAppKey(),
                                        companyAppsAuth.getApiId(), todayDate));
                            }
                        }
                    }
                    newEndTime = calculateTime(dateTime, billingRules.getBillingCycle());
                    operatorBalanceKey = RedisKeyUtil.getCustomerBalanceKeyByDate(app.getAppKey(), todayDate);
                    operatorBalanceValue = app.getPrice().toString();
                    initBalanceValue = companyAppsVo.getBalance().toString();
                    operatorLimitKey = RedisKeyUtil.getCustomerLimitKeyByDate(app.getAppKey(), todayDate);
                    int limit = billingRules.getBillingCycleLimit().intValue() + remainLimit;
                    operatorLimitValue = Integer.toString(limit);
                    initLimitValue = "0";
                    customerChargeMessageVo.setCount(limit);
                    customerChargeMessageVo.setMoney(app.getPrice().negate());
                } else {
                    // 按条收费, 删除接口级别包时计费的调用条数
                    if (null != list && list.size() > 0) {
                        for (CompanyAppsAuth companyAppsAuth : list) {
                            apiBillingRules = billingRulesMapper.selectByBillingRulesUuid(companyAppsAuth.getStrategyUuid());
                            if (apiBillingRules.getBillingType() == 1)
                                delKeys.add(RedisKeyUtil.getAuthLimitKeyByDate(app.getAppKey(),
                                        companyAppsAuth.getApiId(), todayDate));
                        }
                    }
                }

            }

            if (operatorBalanceKey != null) {
                // 设置余额
                BigDecimal balance = RedisUtil.get(operatorBalanceKey, BigDecimal.class);
                if (balance != null)
                    app.setBalance(balance.subtract(app.getPrice()));
                else
                    app.setBalance(new BigDecimal(initBalanceValue).subtract(app.getPrice()));
            }
            if (operatorLimitValue != null) {
                // 设置stock
                Long limit = RedisUtil.get(operatorLimitKey, Long.class);
                if (limit != null)
                    app.setStock(limit + Long.parseLong(operatorLimitValue));
                else
                    app.setStock(Long.parseLong(initLimitValue) + Long.parseLong(operatorLimitValue));
            }
            // 校验当前客户下所有合约的价格和收费模式
            if (null != list && list.size() > 0) {
                list.forEach(companyAppsAuth -> {
                    companyAppsAuth.setStrategyUuid(app.getStrategyUuid());
                    companyAppsAuth.setPrice(app.getPrice());
                    authService.checkChargeMode(companyAppsAuth);
                    DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId( new JSONObject().fluentPut("apiId", companyAppsAuth.getApiId()));
                    authService.checkPrice(companyAppsAuth,datasourceCharge);
                });
            }
            // 修改绑定在该appKey下所有的合约的相关价格
            if (list != null && list.size() > 0) {
                Long finalNewEndTime = newEndTime;
                list.forEach(companyAppsAuth -> {
                    companyAppsAuth.setStrategyUuid(app.getStrategyUuid());
                    companyAppsAuth.setPrice(app.getPrice());
                    companyAppsAuth.setStock(null);
                    companyAppsAuth.setUpdateTime(now);
                    if (finalNewEndTime != null) {
                        companyAppsAuth.setStartTime(now);
                        companyAppsAuth.setEndTime(finalNewEndTime);
                    }
                    companyAppsAuthMapper.updateByPrimaryKey(companyAppsAuth);
                });
            }

        } else {
            // 客户修改后没有计费规则,需判断修改前是否具有计费规则
            // 修改客户表的余量和价格
            app.setStock(null);
            app.setPrice(null);
            if (StringUtils.isNotBlank(companyAppsVo.getStrategyUuid())) {
                // 客户修改前具有计费规则,删除appKey相关合约
                CompanyAppsAuth paramAuth = new CompanyAppsAuth();
                paramAuth.setAppKey(companyAppsVo.getAppKey());
                companyAppsAuthMapper.delete(paramAuth);
                delKeys.add(RedisKeyUtil.getCustomerLimitKeyByDate(companyAppsVo.getAppKey(),todayDate));
                customerChargeMessageVo = CustomerChargeMessageVo.builder()
                        .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                        .appKey(app.getAppKey())
                        .money(BigDecimal.ZERO)
                        .count(0)
                        .isDeleteCustomerBalance(false)
                        .isDeleteCustomerCount(true)
                        .isDeleteAuthCount(false)
                        .build();

            }
        }
        // 更新客户
        companyAppsMapper.updateByPrimaryKeySelective(app);
        CompanyAppsVersion appsVersion = new CompanyAppsVersion();
        appsVersion.setAppKey(app.getAppKey());
        appsVersion.setStrategyUuid(app.getStrategyUuid());
        appsVersion.setCreateTime(now);
        appsVersion.setUpdateTime(now);
        appsVersion.setPrice(app.getPrice());
        companyAppsVersionMapper.insertSelective(appsVersion);
        // 进行redis 同步更新余额和余量
        if (operatorBalanceKey != null) {
            Integer operatorStatus = RedisLuaOperateUtil.deRedisBalance(operatorBalanceKey, initBalanceValue, operatorBalanceValue);
            if (operatorStatus == 1)
                throw new DataValidationException("客户的余额不足以购买所选择的包时计费套餐");
            // 增加调用余量
            RedisLuaOperateUtil.upRedisBalance(operatorLimitKey, initLimitValue, operatorLimitValue);
        }
        // 删除相关余量缓存
        delKeys.forEach(RedisUtil::delete);
        RespCompanyKey respCompanyKey = companyAppsMapper.selectKey(app.getAppKey());
        app.setPrivateKey(respCompanyKey.getPrivateKey());
        app.setPublicKey(respCompanyKey.getPublicKey());
        // 更新redis中存储的客户信息
        RedisUtil.setToCaches(RedisKeyUtil.getCustomerInfoKey(app.getAppKey()), app);
        if (customerChargeMessageVo != null) {
            // 发送变更消息到flink
            CustomerChargeMessageVo finalCustomerChargeMessageVo = customerChargeMessageVo;
            new MessageHandler() {
                @Override
                public void handle() {
                    try {
                        String message = JSON.toJSONString(finalCustomerChargeMessageVo);
                        kafkaService.sendCustomerFlink(message);
                        logger.info("编辑客户发送kafka消息到flink: {}", message);
                    } catch (Exception e) {
                        logger.error("编辑客户发送kafka消息到flink失败: {}", e);
                    }
                }
            }.putQueue();
        }
    }

    private long calculateTime(LocalDateTime dateTime, Integer billingCycle) {
        LocalDateTime updateTime = null;
        switch (billingCycle) {
            case 1:
                updateTime = dateTime.plusYears(1);
                break;
            case 2:
                updateTime = dateTime.plusMonths(3);
                break;
            case 3:
                updateTime = dateTime.plusMonths(1);
                break;
            default:
                break;
        }
        if (updateTime == null)
            throw new DataValidationException("修改客户合约时间失败");
        return updateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * @Description : 删除客户
     * @param companyApps
     * @Return : void
     * @Date : 2020/4/16 17:07
     */
    @Override
    @Transactional
    public void deleteCustomer(CompanyApps companyApps) {
        String todayDate = DateUtil.getTodayDate();
        List<String> delKeys = new ArrayList<>();
        companyAppsMapper.deleteByAppKey(companyApps.getAppKey());
        delKeys.add(RedisKeyUtil.getCustomerBalanceKeyByDate(companyApps.getAppKey(),todayDate));
        delKeys.add(RedisKeyUtil.getCustomerLimitKeyByDate(companyApps.getAppKey(), todayDate));
        // 删除相关合约
        List<CompanyAppsAuth> list = companyAppsAuthMapper.findAppKeyAndApiId(companyApps.getAppKey(), null, null);
        if (list != null)
            list.forEach(companyAppsAuth ->{
                        delKeys.add(RedisKeyUtil
                                .getAuthLimitKeyByDate(companyApps.getAppKey(), companyAppsAuth.getApiId(), todayDate));
                        companyAppsAuthMapper.deleteById(companyAppsAuth.getId());
                    });

        // 删除客户的缓存
        delKeys.add(RedisKeyUtil.getCustomerInfoKey(companyApps.getAppKey()));
        // 删除相关合约的缓存
        delKeys.forEach(RedisUtil::delete);
        CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                .appKey(companyApps.getAppKey())
                .isDeleteCustomerBalance(true)
                .build();
        new MessageHandler() {
            @Override
            public void handle() {
                try {
                    String message = JSON.toJSONString(customerChargeMessageVo);
                    kafkaService.sendCustomerFlink(message);
                    logger.debug("删除客户发送kafka消息到flink: {}", message);
                } catch (Exception e) {
                    logger.error("删除客户发送kafka消息到flink失败: {}", e);
                }
            }
        }.putQueue();
    }

    @Override
    public List<CompanyApps> selectCompanyByStrategyUuid(String strategyUuid) {
        return companyAppsMapper.selectCompanyByStrategyUuid(strategyUuid);
    }

    /**
     * @Description : 客户余额余量记录 分页查询
     * @Date : 2020/4/1 10:12
     * @Auth : qiuwen
     */
    @Override
    public PageInfo<RespCompanyBalanceHistory> queryBalancePage(ReqAppkeyByPage req) {
        PageHelper.startPage(req.getPageNum(),req.getPageSize());
        List<RespCompanyBalanceHistory> list = companyBalanceHistoryMapper.listByAppKey(req.getAppKey());
        return new PageInfo<>(list);
    }


}
