package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.service.CompareFlinkAndRedisJobService;
import com.imjcker.manager.charge.vo.AuthSetVO;
import com.imjcker.manager.charge.vo.FlinkCommpanyRedisVO;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisKeyUtil;
import com.lemon.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Author WT
 * @Date 10:49 2020/4/26
 * @Version CompareFlinkAndRedisJobServiceImpl v1.0
 * @Desicrption
 */
@Slf4j
@Service
public class CompareFlinkAndRedisJobServiceImpl implements CompareFlinkAndRedisJobService {


    @Autowired
    private CompanyAppsMapper companyAppsMapper;

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;

    @Autowired
    private CompanyBalanceHistoryMapper companyBalanceHistoryMapper;

    @Autowired
    private CustomerDifferentBalanceMapper customerDifferentBalanceMapper;

    @Autowired
    private AuthStockHistoryMapper authStockHistoryMapper;

    @Autowired
    private AuthDifferentStockMapper authDifferentStockMapper;

    /**
     * @Description : 每天凌晨五点对账flink
     * @param
     * @Return : void
     * @Date : 2020/4/26 10:56
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reconciliationFlinkBalance() {
        CompanyApps param = new CompanyApps();
        param.setStatusFlag(1);
        List<CompanyApps> list = companyAppsMapper.select(param);
        //String[] apps = new String[]{"ft138"};
        //List<CompanyApps> list = companyAppsMapper.listByAppKeys(Arrays.asList(apps));
        String yesterday = DateUtil.getYesterdayDate();
        //String yesterday = "2020-05-20";
        Date yesterdayDate;
        try {
            yesterdayDate = DateUtil.strToDate(yesterday);
        } catch (ParseException e) {
            log.error("时间转换有误,yesterday={}",yesterday);
            throw new BusinessException("定时任务对账fink时间生成有误");
        }
        // 获取前一天的时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        long timeStamp = calendar.getTime().getTime();
        log.info("昨日时间戳: {}", timeStamp);
        long now = System.currentTimeMillis();
        if (list != null && list.size() > 0) {
            if(list.size()>100){
                log.error("警告，对账查询appKey对象长度达到：{}",list.size());
            }
            List<String> delList = new ArrayList<>();
            List<CustomerDifferentBalance> insertList = Lists.newArrayListWithCapacity(100);
            List<CompanyBalanceHistory> companyBalanceHistoryList = Lists.newArrayListWithCapacity(100);
            List<AuthStockHistory> authStockList = Lists.newArrayListWithCapacity(100);
            list.forEach(companyApps -> {
                //获取flink昨日记录余额 余量
                System.out.println(RedisKeyUtil.getFinkHistoryKey(yesterday,companyApps.getAppKey()));
                FlinkCommpanyRedisVO flinkCommpanyRedisVO =
                        RedisUtil.get(RedisKeyUtil.getFinkHistoryKey(yesterday,companyApps.getAppKey()), FlinkCommpanyRedisVO.class);
                if (flinkCommpanyRedisVO == null || flinkCommpanyRedisVO.getBalance()==null) {
                    log.error("" +
                            "={},flinkCommpanyRedisVO对象为空",companyApps.getAppKey());
                    return;
                }
                List<AuthSetVO> authSet = flinkCommpanyRedisVO.getAuthSet();
                if (StringUtils.isNotBlank(companyApps.getStrategyUuid())) {
                    log.info("对比客户的余额和余量,appKey:{}", companyApps.getAppKey());
                    // 对比客户的余额和余量

                    // 获取redis中昨日的余额和余量
                    String customerYesterdayBalanceKey = RedisKeyUtil
                            .getCustomerBalanceKeyByDate(companyApps.getAppKey(), yesterday);
                    BigDecimal yesterdayBalance = RedisUtil.get(customerYesterdayBalanceKey, BigDecimal.class);

                    String customerYesterdayStockKey = RedisKeyUtil
                            .getCustomerLimitKeyByDate(companyApps.getAppKey(), yesterday);
                    Integer yesterdayStock = RedisUtil.get(customerYesterdayStockKey, Integer.class);

                    // 获取差值
                    CustomerDifferentBalance insert = new CustomerDifferentBalance();
                    insert.setAppKey(companyApps.getAppKey());
                    insert.setCreateTime(now);
                    if (yesterdayBalance != null) {
                        delList.add(customerYesterdayBalanceKey);
                        BigDecimal dBalanceValue = yesterdayBalance.subtract(flinkCommpanyRedisVO.getBalance());
                        insert.setBalanceValue(dBalanceValue);
                    }
                    if (yesterdayStock != null) {
                        delList.add(customerYesterdayStockKey);
                        Integer dStockValue = yesterdayStock - flinkCommpanyRedisVO.getStock();
                        insert.setStockValue(dStockValue);
                    }
                    insertList.add(insert);
                    //customerDifferentBalanceMapper.insertSelective(insert);
                } else {
                    log.info("对比客户的余额和合约的余量,appKey: {}", companyApps.getAppKey());
                    // 对比客户的余额和合约的余量
                    List<CompanyAppsAuth> authList = companyAppsAuthMapper
                            .findAppKeyAndApiId(companyApps.getAppKey(), null, null);

                    // 对比客户余额
                    /*RespCompanyBalanceHistory balanceHistory =
                            companyBalanceHistoryMapper.findNewestBalanceByAppKey(companyApps.getAppKey(), timeStamp);*/

                    String customerYesterdayBalanceKey = RedisKeyUtil
                            .getCustomerBalanceKeyByDate(companyApps.getAppKey(), yesterday);
                    BigDecimal yesterdayBalance = RedisUtil.get(customerYesterdayBalanceKey, BigDecimal.class);
                    // 获取差值
                    CustomerDifferentBalance insert = new CustomerDifferentBalance();
                    insert.setAppKey(companyApps.getAppKey());
                    insert.setCreateTime(now);
                    if (yesterdayBalance != null) {
                        delList.add(customerYesterdayBalanceKey);
                        BigDecimal dBalanceValue = yesterdayBalance.subtract(flinkCommpanyRedisVO.getBalance());
                        insert.setBalanceValue(dBalanceValue);
                    }
                    insertList.add(insert);
                    //customerDifferentBalanceMapper.insertSelective(insert);

                    // 对比合约余量
                    if (authList != null) {
                        //把list处理成对应的map<appId,count>
                        Map<Integer, Integer> map = delMap(authSet);
                        List<AuthDifferentStock> authDifferentStockList = Lists.newArrayListWithCapacity(authList.size());
                        authList.forEach(companyAppsAuth -> {
                            /*RespAuthStockHistory respAuthStockHistory = authStockHistoryMapper.findNewestAuthStock(
                                    companyApps.getAppKey(), companyAppsAuth.getApiId(), timeStamp);*/
                            Integer count = map.get(companyAppsAuth.getApiId());

                            if (count != null) {
                                String authYesterdayStockKey = RedisKeyUtil.getAuthLimitKeyByDate(
                                        companyApps.getAppKey(), companyAppsAuth.getApiId(), yesterday);
                                Integer yesterdayStock = RedisUtil.get(authYesterdayStockKey, Integer.class);

                                AuthDifferentStock authDifferentStock = new AuthDifferentStock();
                                authDifferentStock.setAppKey(companyApps.getAppKey());
                                authDifferentStock.setApiId(companyAppsAuth.getApiId());
                                authDifferentStock.setCreateTime(now);
                                if (yesterdayStock != null) {
                                    delList.add(authYesterdayStockKey);
                                    Integer dStockValue = yesterdayStock - count;
                                    authDifferentStock.setStockValue(dStockValue);
                                }
                                authDifferentStockList.add(authDifferentStock);
                                //authDifferentStockMapper.insertSelective(insert);
                            }
                        });
                        if(authDifferentStockList.size()>0){
                            authDifferentStockMapper.insertByList(authDifferentStockList);
                        }
                    }else{
                        log.error("客户合约集合为空");
                    }
                }
                if(insertList.size()==100){
                    customerDifferentBalanceMapper.insertByList(insertList);
                    //批量插入100条之后 清空list
                    insertList.clear();
                }

                //1.客户级余量记录插入
                CompanyBalanceHistory companyInsert = new CompanyBalanceHistory();
                companyInsert.setAppKey(flinkCommpanyRedisVO.getAppKey());
                companyInsert.setBalance(flinkCommpanyRedisVO.getBalance());
                companyInsert.setStock(flinkCommpanyRedisVO.getStock());
                companyInsert.setRecordTime(yesterdayDate);
                companyInsert.setCreateTime(now);
                //companyBalanceHistoryMapper.insertSelective(companyInsert);
                companyBalanceHistoryList.add(companyInsert);
                if(companyBalanceHistoryList.size()==100){
                    //批量插入
                    companyBalanceHistoryMapper.insertByList(companyBalanceHistoryList);
                    companyBalanceHistoryList.clear();
                }
                //2.客户对应合约余量 批量插入
                authSet.forEach(auth -> {
                    AuthStockHistory authInsert = new AuthStockHistory();
                    authInsert.setAppKey(flinkCommpanyRedisVO.getAppKey());
                    authInsert.setApiId(auth.getApiId());
                    authInsert.setStock(auth.getCount());
                    authInsert.setRecordTime(yesterdayDate);
                    authInsert.setCreateTime(now);
                    authStockList.add(authInsert);
                    if(authStockList.size()==100){
                        authStockHistoryMapper.inserByList(authStockList);
                        authStockList.clear();
                    }
                });
            });
            //批量操作  差值对象 list
            if(insertList.size()>0){
                customerDifferentBalanceMapper.insertByList(insertList);
            }
            //批量操作  客户级余量记录
            if(companyBalanceHistoryList.size()>0){
                companyBalanceHistoryMapper.insertByList(companyBalanceHistoryList);
            }
            //批量操作  客户对应合约余量
            if(authStockList.size()>0){
                authStockHistoryMapper.inserByList(authStockList);
            }
            // 把昨日的key设置15天过期
            delList.forEach(RedisUtil::keyExpire15Days);
        }
    }

    private Map<Integer,Integer> delMap(List<AuthSetVO> list){
        Map<Integer,Integer> map = new HashMap<>();
        list.forEach(authSetVO -> map.put(authSetVO.getApiId(),authSetVO.getCount()));
        return map;
    }
}
