package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.plugin.queue.MessageHandler;
import com.imjcker.manager.charge.service.AuthService;
import com.imjcker.manager.charge.service.DatasourceChargeService;
import com.imjcker.manager.charge.service.KafkaService;
import com.imjcker.manager.charge.service.RedisService;
import com.imjcker.manager.charge.utils.ConstantEnum;
import com.imjcker.manager.charge.utils.RedisLuaOperateUtil;
import com.imjcker.manager.charge.vo.request.ReqStockByPage;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.util.RedisKeyUtil;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisUtil;
import com.lemon.common.vo.CustomerChargeMessageVo;
import com.lemon.common.vo.MessageModeEnum;
import com.lemon.common.vo.ResultStatusEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;

    @Autowired
    private CompanyAppsAuthVersionMapper companyAppsAuthVersionMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CompanyAppsMapper companyAppsMapper;

    @Autowired
    private BillingRulesMapper billingRulesMapper;

    @Autowired
    private DatasourceChargeService datasourceChargeService;

    @Autowired
    private AuthStockHistoryMapper authStockHistoryMapper;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 删除合约
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean delete(Integer id) {
        List<CompanyAppsAuth> list = companyAppsAuthMapper.findAppKeyAndApiId(null, null, id);
        String today = DateUtil.getTodayDate();
        if (!list.isEmpty()) {
            CompanyAppsAuth companyAppsAuth = list.get(0);
            //删除客户appKey存在的合约
            companyAppsAuthMapper.deleteById(id);
            String appKey = companyAppsAuth.getAppKey();
            Integer apiId = companyAppsAuth.getApiId();
            //同步更新redis缓存
            RedisUtil.delete(RedisKeyUtil.getAuthInfoKey(appKey, apiId));

            //删除计数key
            RedisUtil.delete(RedisKeyUtil.getAuthLimitKeyByDate(appKey, apiId, today));
            // 发送消息到flink
            CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                    .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                    .appKey(appKey)
                    .apiId(apiId)
                    .money(BigDecimal.ZERO)
                    .count(0)
                    .isDeleteCustomerBalance(false)
                    .isDeleteCustomerCount(false)
                    .isDeleteAuthCount(true)
                    .build();
            new MessageHandler() {
                @Override
                public void handle() {
                    try {
                        String message = JSON.toJSONString(customerChargeMessageVo);
                        kafkaService.sendCustomerFlink(message);
                        logger.debug("删除合约发送消息到flink成功: {}", message);
                    } catch (Exception e) {
                        logger.error("删除合约发送消息到flink失败: {}", e);
                    }
                }
            }.putQueue();
        } else {
            throw new DataValidationException(ExceptionInfo.NOT_EXIST_ID);
        }
        return true;
    }

    /**
     * 合约搜索
     *
     * @param jsonObject
     * @return
     */
    @Override
    public PageInfo<CompanyAppsAuthVo> search(JSONObject jsonObject) {
        AuthParam authParam = jsonObject.toJavaObject(AuthParam.class);
        PageHelper.startPage(authParam.getPageNum() == null ? 1 : authParam.getPageNum(),
                authParam.getPageSize() == null ? 10 : authParam.getPageSize());
        List<CompanyAppsAuthVo> companyAppsAuthVoList = companyAppsAuthMapper.queryAuth(authParam);
        // 从缓存中获取调用余量
        String today = DateUtil.getTodayDate();
        companyAppsAuthVoList.forEach(companyAppsAuthVo -> {
            Long stock = RedisUtil.get(RedisKeyUtil
                    .getAuthLimitKeyByDate(companyAppsAuthVo.getAppKey(),
                            companyAppsAuthVo.getApiId(), today), Long.class);
            if (null != stock) companyAppsAuthVo.setStock(stock);
        });
        return new PageInfo<>(companyAppsAuthVoList);
    }

    /**
     * 新增合约
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @Override
    @Transactional
    public boolean save(JSONObject jsonObject) {
        checkParam(jsonObject);
        //校验是否存在
        CompanyAppsAuth companyAppsAuth = jsonObject.toJavaObject(CompanyAppsAuth.class);
        isExist(companyAppsAuth.getAppKey(), companyAppsAuth.getApiId());
        // 查询appKey是否绑定计费策略
        CompanyAppsVo appsVo = companyAppsMapper.findCustomerByAppKey(companyAppsAuth.getAppKey());
        if (appsVo == null) {
            throw new DataValidationException("appKey 不存在");
        }
        if (StringUtils.isNotBlank(appsVo.getStrategyUuid())) {
            // 同一appKey下的所有合约需保持一致
            companyAppsAuth.setStrategyUuid(appsVo.getStrategyUuid());
            companyAppsAuth.setPrice(appsVo.getPrice());
        }
        if (StringUtils.isBlank(companyAppsAuth.getStrategyUuid()))
            throw new DataValidationException("计费规则不能为空");

        checkChargeMode(companyAppsAuth);
        DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId( new JSONObject().fluentPut("apiId", companyAppsAuth.getApiId()));
        checkPrice(companyAppsAuth,datasourceCharge);
        long now = System.currentTimeMillis();
        companyAppsAuth.setUpdateTime(now);
        companyAppsAuth.setCreateTime(now);
        BillingRules billingRules = billingRulesMapper.selectByBillingRulesUuid(companyAppsAuth.getStrategyUuid());
        //处理时间
        delTime(billingRules,companyAppsAuth);
        if (billingRules.getBillingCycleLimit() != null) {
            companyAppsAuth.setStock(billingRules.getBillingCycleLimit());
        }
        companyAppsAuthMapper.insertSelective(companyAppsAuth);
        // 设置价格记录
        CompanyAppsAuthVersion authVersion = new CompanyAppsAuthVersion();
        authVersion.setApiId(companyAppsAuth.getApiId());
        authVersion.setAppKey(companyAppsAuth.getAppKey());
        authVersion.setStartTime(companyAppsAuth.getStartTime());
        authVersion.setEndTime(companyAppsAuth.getEndTime());
        authVersion.setCreateTime(companyAppsAuth.getCreateTime());
        authVersion.setUpdateTime(companyAppsAuth.getUpdateTime());
        authVersion.setStrategyUuid(companyAppsAuth.getStrategyUuid());
        authVersion.setPrice(companyAppsAuth.getPrice());
        companyAppsAuthVersionMapper.insertSelective(authVersion);
        //更新redis缓存
        String appKeyStr =companyAppsAuth.getAppKey();
        Integer apiId = companyAppsAuth.getApiId();
        //同步更新redis缓存
        String authKey = RedisKeyUtil.getAuthInfoKey(appKeyStr, apiId);
        RedisUtil.setToCaches(authKey,companyAppsAuth);
        // 查询计费规则下的调用次数限制
        if (null != billingRules.getBillingCycleLimit()) {
            String today = DateUtil.getTodayDate();
            //扣redis的钱
            Integer evel = RedisLuaOperateUtil.deRedisBalance(RedisKeyUtil.getCustomerBalanceKeyByDate(appsVo.getAppKey(), today),
                    appsVo.getBalance().toString(), companyAppsAuth.getPrice().toString());
            if(evel==1){
                logger.error("redis key 值不足扣减 key=customer:balance:"+appsVo.getAppKey());
                throw new BusinessException(ResultStatusEnum.TFB_API_BALANCE_ERROR.getMessage());
            }
            // 构建新增合约发送到flink的消息
            CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                    .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                    .appKey(appKeyStr)
                    .money(companyAppsAuth.getPrice().negate())
                    .count(billingRules.getBillingCycleLimit().intValue())
                    .isDeleteAuthCount(false)
                    .isDeleteCustomerCount(false)
                    .isDeleteCustomerBalance(false)
                    .build();

            // 设置调用次数限制
            if (StringUtils.isNotBlank(appsVo.getStrategyUuid())) {
                // 增加appKey 级别的调用次数
                RedisLuaOperateUtil.upRedisBalance(RedisKeyUtil.getCustomerLimitKeyByDate(appsVo.getAppKey(), today),
                        "0", billingRules.getBillingCycleLimit().toString());
            } else {
                // 增加接口级别的调用次数
                String authLimit = RedisKeyUtil.getAuthLimitKeyByDate(companyAppsAuth.getAppKey(),
                        companyAppsAuth.getApiId(), today);
                RedisUtil.setToCaches(authLimit, billingRules.getBillingCycleLimit().intValue());
                customerChargeMessageVo.setApiId(apiId);
            }

            new MessageHandler() {
                @Override
                public void handle() {
                    try {
                        String message = JSON.toJSONString(customerChargeMessageVo);
                        kafkaService.sendCustomerFlink(message);
                        logger.info("新增合约发送消息到flink成功: {}", message);
                    } catch (Exception e) {
                        logger.error("新增合约发送消息到flink失败: {}", e);
                    }
                }
            }.putQueue();
        }
        return true;
    }

    /**
     * 处理startTime 和 endTime
     * @param billingRules
     * @param companyAppsAuth
     */
    private void delTime(BillingRules billingRules,CompanyAppsAuth companyAppsAuth){
        if(billingRules.getBillingType()== ConstantEnum.BillingType.billingBasedTime.getValue()){
            Integer billingCycle = billingRules.getBillingCycle();
            Calendar instance = Calendar.getInstance();
            Date nowDate = new Date();
            companyAppsAuth.setStartTime(nowDate.getTime());
            instance.setTime(nowDate);
            if(billingCycle==1){
                //年
                instance.add(Calendar.YEAR,1);
            }
            if(billingCycle==2){
                //季度
                instance.add(Calendar.MONTH,3);
            }
            if(billingCycle==3){
                //月
                instance.add(Calendar.MONTH,1);
            }
            companyAppsAuth.setEndTime(instance.getTime().getTime());
        }else if(billingRules.getBillingType()== ConstantEnum.BillingType.billingFees.getValue()){
            if(companyAppsAuth.getStartTime()==null || companyAppsAuth.getEndTime()==null){
                throw new DataValidationException("按条计费起始、结束时间不能为空");
            }
            if(companyAppsAuth.getEndTime()<=companyAppsAuth.getStartTime()){
                throw new DataValidationException("时间区间不正确");
            }
        }
    }


    /**
     * 编辑合约
     *
     * @param jsonObject
     * @return
     * @throws
     */
    @Override
    @Transactional
    public boolean edit(JSONObject jsonObject) {
        checkParam(jsonObject);
        CompanyAppsAuth appsAuth = jsonObject.toJavaObject(CompanyAppsAuth.class);
        long now = System.currentTimeMillis();
        // 查询appKey是否绑定计费策略
        CompanyAppsVo appsVo = companyAppsMapper.findCustomerByAppKey(appsAuth.getAppKey());
        if (appsVo == null) {
            throw new DataValidationException("appKey 不存在");
        }
        if (StringUtils.isNotBlank(appsVo.getStrategyUuid())) {
            throw new DataValidationException("该合约的appKey已经绑定了计费策略,不能单独更改接口合约策略");
            // 同一appKey下的所有合约需保持一致
//            appsAuth.setStrategyUuid(appsVo.getBillingRules().getUuid());
        }

        checkChargeMode(appsAuth);
        DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId( new JSONObject().fluentPut("apiId", appsAuth.getApiId()));
        checkPrice(appsAuth,datasourceCharge);

        //处理时间
        BillingRules billingRules = billingRulesMapper.selectByBillingRulesUuid(appsAuth.getStrategyUuid());
        delTime(billingRules,appsAuth);
        List<CompanyAppsAuth> list = companyAppsAuthMapper.findAppKeyAndApiId(null, null, appsAuth.getId());

        CompanyAppsAuthVersion authVersion = new CompanyAppsAuthVersion();
        authVersion.setApiId(appsAuth.getApiId());
        authVersion.setAppKey(appsAuth.getAppKey());
        authVersion.setStartTime(appsAuth.getStartTime());
        authVersion.setEndTime(appsAuth.getEndTime());
        authVersion.setCreateTime(now);
        authVersion.setUpdateTime(now);
        authVersion.setStrategyUuid(appsAuth.getStrategyUuid());
        authVersion.setPrice(appsAuth.getPrice());
        // 插入接口计费历史价格表
        companyAppsAuthVersionMapper.insertSelective(authVersion);
        appsAuth.setUpdateTime(now);
        companyAppsAuthMapper.updateByPrimaryKeySelective(appsAuth);
        //更新redis缓存
        String appKeyStr = appsAuth.getAppKey();
        Integer apiId = appsAuth.getApiId();
        //同步更新redis缓存
        String authKey = RedisKeyUtil.getAuthInfoKey(appKeyStr, apiId);
        RedisUtil.setToCaches(authKey, appsAuth);
        String today = DateUtil.getTodayDate();
        // 构建修改合约发送到flink的消息
        CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                .messageMode(MessageModeEnum.CUSTOMER_EDIT.getCode())
                .appKey(appKeyStr)
                .apiId(apiId)
                .money(BigDecimal.ZERO)
                .count(0)
                .isDeleteCustomerBalance(false)
                .isDeleteCustomerCount(false)
                .isDeleteAuthCount(false)
                .build();
        if(billingRules.getBillingType()== ConstantEnum.BillingType.billingBasedTime.getValue()){
            if(list==null || list.size()==0){
                throw new DataValidationException("传入ID有误");
            }
            BillingRules oldBillingRules = billingRulesMapper.selectByBillingRulesUuid(list.get(0).getStrategyUuid());
            Long initValue = oldBillingRules.getBillingCycleLimit() ==null ? 0L: oldBillingRules.getBillingCycleLimit();

            //扣redis的钱
            Integer evel = RedisLuaOperateUtil.deRedisBalance(RedisKeyUtil.getCustomerBalanceKeyByDate(appsVo.getAppKey(), today),
                    appsVo.getBalance().toString(), appsAuth.getPrice().toString());
            if(evel==1){
                logger.error("redis key 值不足扣减 key=customer:balance:"+appsVo.getAppKey());
                throw new BusinessException(ResultStatusEnum.TFB_API_BALANCE_ERROR.getMessage());
            }
            customerChargeMessageVo.setMoney(appsAuth.getPrice().negate());
            customerChargeMessageVo.setCount(billingRules.getBillingCycleLimit().intValue());
            //如果当前续约是按时间  增加条数、或初始化条数
            RedisLuaOperateUtil.upRedisBalance(RedisKeyUtil.getAuthLimitKeyByDate(appsVo.getAppKey(), appsAuth.getApiId(), today),
                    initValue.toString(), billingRules.getBillingCycleLimit().toString());
        }else if(billingRules.getBillingType()== ConstantEnum.BillingType.billingFees.getValue()){
            //当前续约为按条，删除计数key
            RedisUtil.delete(RedisKeyUtil.getAuthLimitKeyByDate(appsAuth.getAppKey(), appsAuth.getApiId(), today));
            customerChargeMessageVo.setIsDeleteAuthCount(true);
        }

        new MessageHandler() {
            @Override
            public void handle() {
                try {
                    String message = JSON.toJSONString(customerChargeMessageVo);
                    kafkaService.sendCustomerFlink(message);
                    logger.debug("修改合约发送消息到flink成功: {}", message);
                } catch (Exception e) {
                    logger.error("修改合约发送消息到flink失败: {}", e);
                }
            }
        }.putQueue();
        return true;
    }

    /**
     * appKey+apiId是否存在有效关系
     *
     * @param
     * @return
     */
    public void isExist(String appKeyStr, Integer apiId) {
        if (StringUtils.isBlank(appKeyStr) || null == apiId) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        List<CompanyAppsAuth> appKeyList = companyAppsAuthMapper.findAppKeyAndApiId(appKeyStr, apiId, null);//精确查询数据库所有记录
        if (null != appKeyList && appKeyList.size() > 0)
            throw new BusinessException(appKeyStr + ":" + apiId + ExceptionInfo.EXIST);
    }

    /**
     * 参数检测
     *
     * @param jsonObject
     */
    public void checkParam(JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        CompanyAppsAuth appKey = jsonObject.toJavaObject(CompanyAppsAuth.class);
        if (StringUtils.isBlank(appKey.getAppKey()) || null == appKey.getApiId())
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
    }

    /**
     * 根据apiId删除记录,主要用于接口删除时同步删除该接口下的合约
     *
     * @param apiId
     */
    @Override
    public void deleteByApiId(Integer apiId) {
        companyAppsAuthMapper.deleteByApiId(apiId);
        redisService.keysDel("auth:" + "*:" + apiId);
    }

    @Override
    public List<CompanyAppsAuth> selectAuthByStrategyUuid(String strategyUuid) {
        return companyAppsAuthMapper.selectAuthByStrategyUuid(strategyUuid);
    }

    /**
     * 定价校验
     * 为了维护成本，需要校验客户定价，需要联系上游接口成本校验下游客户定价
     */
    @Override
    public void checkPrice(CompanyAppsAuth companyAppsAuth,DatasourceCharge datasourceCharge) {
        Integer apiId = companyAppsAuth.getApiId();
        if(companyAppsAuth.getEndTime() > datasourceCharge.getEndTime()){
            throw new BusinessException("合约时间大于上游计费合同时间，apiId="+apiId);
        }
        //获取下游客户计费信息
        BigDecimal customerPrice = companyAppsAuth.getPrice();
        int customerDays = DateUtil.getDayBetween(companyAppsAuth.getStartTime(), companyAppsAuth.getEndTime());
        String customerUuid = companyAppsAuth.getStrategyUuid();
        BillingRules customereRules = billingRulesMapper.selectByBillingRulesUuid(customerUuid);
        Long customerLimit = customereRules.getBillingCycleLimit();
        Integer custometBillingType = customereRules.getBillingType();
        //根据apiId获取上游计费信息  修改数据源的话是传入
        //DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId( new JSONObject().fluentPut("apiId", apiId));//(TODO)
        BigDecimal datasourcePrice = datasourceCharge.getPrice();
        if(datasourcePrice==null){
            throw new DataValidationException("价格字段有误");
        }
        int datasourceDays = DateUtil.getDayBetween(datasourceCharge.getStartTime(), datasourceCharge.getEndTime());
        String datasourceChargeUuid = datasourceCharge.getBillingRulesUuid();
        BillingRules datasourceRules = billingRulesMapper.selectByBillingRulesUuid(datasourceChargeUuid);//根据数据源uuid获取计费规则
        Long datasourceLimit = datasourceRules.getBillingCycleLimit();
        Integer datasourceBillingType = datasourceRules.getBillingType();
        if (2 == datasourceBillingType) {//上游按条计费
            if (2 == custometBillingType && (customerPrice.compareTo(datasourcePrice) < 0)) {
                throw new PerDeficitException("每笔调用" + ExceptionInfo.CHARGE_DEFICIT + (datasourcePrice.subtract(customerPrice)));
            } else if (1 == custometBillingType && null != customerLimit && -1 != customerLimit) {
                //以数据源的成本价计算最大调用次数，客户周期内超过最大调用次数会产生亏损
                long customerNumMax = customerPrice.divide(datasourcePrice,6,BigDecimal.ROUND_HALF_DOWN).longValue();
                if (customerLimit > customerNumMax)
                    throw new ThanMaxException(ExceptionInfo.CHARGE_OVER_MAXNUM + (customerLimit - customerNumMax));
            } else if(1 == custometBillingType && null != customerLimit && -1 == customerLimit){//包时不限量，无法控制成本
                throw new BusinessException(ExceptionInfo.CHARGE_UNCONTROLLABLE_COST);
            }
        } else if (1 == datasourceBillingType && null != datasourceLimit && -1 != datasourceLimit) {//上游包时限量
            if (2 == customereRules.getBillingType()) {//下游按条计费
                //计算数据源包时内价格按照客户单价可以调用的最大次数，与包时内的限量次数比较
                BigDecimal datasourceMinPrice = datasourcePrice.divide(BigDecimal.valueOf(datasourceLimit),6,BigDecimal.ROUND_HALF_DOWN);
                if (datasourceMinPrice.compareTo(customerPrice)>0)
                    throw new PerDeficitException("每笔调用" + ExceptionInfo.CHARGE_DEFICIT + (datasourceMinPrice.subtract(customerPrice)));
            } else if (1 == custometBillingType && null != customerLimit && -1 != customerLimit) {//包时限量
                //上下游包时限量，比较天成本
                BigDecimal sourceCost = datasourcePrice.divide(BigDecimal.valueOf(datasourceDays),6,BigDecimal.ROUND_HALF_DOWN);
                BigDecimal customerCost = customerPrice.divide(BigDecimal.valueOf(customerDays),6,BigDecimal.ROUND_HALF_DOWN);
                if (sourceCost.compareTo(customerCost) > 0)//下游天成本高于上游
                    throw new DayDeficitException("每天" + ExceptionInfo.CHARGE_DEFICIT  + (sourceCost.subtract(customerCost)));
            }  else if(1 == custometBillingType && null != customerLimit && -1 == customerLimit){
                throw new BusinessException(ExceptionInfo.CHARGE_UNCONTROLLABLE_COST);
            }
        } else {//上游包时不限量
            //下游按条计费无限制
            if (1 == custometBillingType) {//包时（含限量和不限量）
                BigDecimal sourceCost = datasourcePrice.divide(BigDecimal.valueOf(datasourceDays),6,BigDecimal.ROUND_HALF_DOWN);
                BigDecimal customerCost = customerPrice.divide(BigDecimal.valueOf(customerDays),6,BigDecimal.ROUND_HALF_DOWN);
                if (sourceCost.compareTo(customerCost) > 0)//下游天成本高于上游
                    throw new DayDeficitException("每天" + ExceptionInfo.CHARGE_DEFICIT + (sourceCost.subtract(customerCost)));
            }
        }
    }

    /**
     * 校验计费模式  1-查询计费  2-查得计费
     * 上游为查询计费，下游只能为查询计费
     *
     * @param companyAppsAuth
     * @return
     */
    @Override
    public void checkChargeMode(CompanyAppsAuth companyAppsAuth) {
        Integer apiId = companyAppsAuth.getApiId();
        DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId( new JSONObject().fluentPut("apiId", apiId));
        if (null == datasourceCharge) {
            throw new DataValidationException(ExceptionInfo.CHARGE_SOURCE_NOT_EXIT);
        }
        BillingRules datasourceRules = billingRulesMapper.selectByBillingRulesUuid(datasourceCharge.getBillingRulesUuid());
        Integer sourceMode = datasourceRules.getBillingMode();
        BillingRules customereRules = billingRulesMapper.selectByBillingRulesUuid(companyAppsAuth.getStrategyUuid());
        Integer customerMode = customereRules.getBillingMode();
        if ((null == sourceMode || null == customerMode || (1 == sourceMode && 2 == customerMode))) {
            throw new DataValidationException(ExceptionInfo.CHARGE_MODE_NOT_EXIT);
        }
    }

    @Override
    public PageInfo<RespAuthStockHistory> queryStockPage(ReqStockByPage req) {
        PageHelper.startPage(req.getPageNum(),req.getPageSize());
        List<RespAuthStockHistory> list = authStockHistoryMapper.listForPage(req);
        return new PageInfo<>(list);
    }

    /**
     * @Description : 检测apiId是否绑定有合约
     * @param apiId
     * @Return : java.lang.Boolean
     * @Date : 2020/4/16 16:07
     */
    @Override
    public Boolean checkApiAuth(Integer apiId) {
        List<CompanyAppsAuth> authList = companyAppsAuthMapper.findAppKeyAndApiId(null, apiId, null);
        if (authList != null) return authList.size() > 0;
        return false;
    }

    @Override
    public RespCompanyKey selectKey(String appKey) {
        return companyAppsMapper.selectKey(appKey);
    }

    @Override
    @Transactional
    public void updateForKey() {
        List<CompanyApps> companyApps = companyAppsMapper.selectAll();
        for (CompanyApps companyApp : companyApps) {
            KeyPairGenerator general = null;
            try {
                general = KeyPairGenerator.getInstance("RSA");
            } catch (Exception e) {
                throw new DataValidationException("生成客户公钥失败");
            }
            general.initialize(1024);
            KeyPair keyPair = general.generateKeyPair();
            PrivateKey aPrivate = keyPair.getPrivate();
            //System.out.println(new String(Base64.encodeBase64(aPrivate.getEncoded())));
            PublicKey aPublic = keyPair.getPublic();
            //System.out.println(new String(Base64.encodeBase64(aPublic.getEncoded())));
            companyApp.setPrivateKey(new String(Base64.encodeBase64(aPrivate.getEncoded())));
            companyApp.setPublicKey(new String(Base64.encodeBase64(aPublic.getEncoded())));
            companyAppsMapper.updateByPrimaryKeySelective(companyApp);
            RedisUtil.delete(RedisKeyUtil.getCustomerInfoKey(companyApp.getAppKey()));
        }
    }
}
