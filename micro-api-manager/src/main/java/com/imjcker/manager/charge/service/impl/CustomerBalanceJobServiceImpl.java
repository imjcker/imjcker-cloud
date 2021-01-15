package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.imjcker.manager.charge.mapper.CompanyAppsAuthMapper;
import com.imjcker.manager.charge.mapper.CompanyAppsMapper;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.service.CustomerBalanceJobService;
import com.imjcker.manager.charge.service.KafkaService;
import com.imjcker.manager.charge.utils.RedisLuaOperateUtil;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisKeyUtil;
import com.lemon.common.vo.CustomerChargeMessageVo;
import com.lemon.common.vo.MessageModeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WT
 * @Date 10:24 2020/4/14
 * @Version CustomerBalanceJobServiceImpl v1.0
 * @Desicrption
 */
@Service
public class CustomerBalanceJobServiceImpl implements CustomerBalanceJobService {

    private static Logger logger = LoggerFactory.getLogger(CustomerBalanceJobServiceImpl.class);

    @Autowired
    private CompanyAppsMapper companyAppsMapper;

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;

    @Autowired
    private KafkaService kafkaService;

    /**
     * @Description : 每日零点更新redis当日余额和调用余量
     * @param
     * @Return : void
     * @Date : 2020/4/14 10:29
     */
    @Override
    public void updateRedisBalancePerDay() {
        CompanyApps param = new CompanyApps();
        param.setStatusFlag(1);
        List<CompanyApps> list = companyAppsMapper.select(param);
        String today = DateUtil.getTodayDate();
        String yesterday = DateUtil.getYesterdayDate();
        Map<String, String> maps = new HashMap<>();
        if (list != null) {
            list.forEach(companyApps -> {
                // 将客户余额的昨日和今日的redisKey放入map
                maps.put(RedisKeyUtil.getCustomerBalanceKeyByDate(companyApps.getAppKey(),today),
                        RedisKeyUtil.getCustomerBalanceKeyByDate(companyApps.getAppKey(),yesterday));
                if (StringUtils.isBlank(companyApps.getStrategyUuid())) {
                    // 将客户下所有合约的调用余量的昨日和今日的redisKey放入map
                    List<CompanyAppsAuth> authList = companyAppsAuthMapper
                            .findAppKeyAndApiId(companyApps.getAppKey(), null, null);
                    if (authList != null) {
                        authList.forEach(companyAppsAuth -> {
                            String todayKey = RedisKeyUtil.getAuthLimitKeyByDate(companyApps.getAppKey(),
                                    companyAppsAuth.getApiId(), today);
                            String yesterdayKey = RedisKeyUtil.getAuthLimitKeyByDate(companyApps.getAppKey(),
                                    companyAppsAuth.getApiId(), yesterday);
                            maps.put(todayKey, yesterdayKey);
                        });
                    }
                } else {
                    // 将客户的调用余量的昨日和今日redisKey放入map
                    String todayKey = RedisKeyUtil.getCustomerLimitKeyByDate(companyApps.getAppKey(), today);
                    String yesterdayKey = RedisKeyUtil.getCustomerLimitKeyByDate(companyApps.getAppKey(), yesterday);
                    maps.put(todayKey, yesterdayKey);
                }
                try {
                    CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                            .appKey(companyApps.getAppKey())
                            .messageMode(MessageModeEnum.CUSTOMER_INIT.getCode())
                            .build();
                    String message = JSON.toJSONString(customerChargeMessageVo);
                    kafkaService.sendCustomerFlink(message);
                    logger.debug("初始化定时任务发送消息到flink成功: {}", message);
                } catch (Exception e) {
                    logger.error("初始化定时任务发送消息到flink失败: {}", e);
                }
            });
            if (maps.size() > 0) {
                maps.forEach(RedisLuaOperateUtil::updateTodayRedisBalance);
            }
        }
    }
}
