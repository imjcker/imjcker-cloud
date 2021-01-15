//package com.imjcker.gateway.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.imjcker.api.common.util.DateUtil;
//import com.imjcker.api.common.util.RedisClusterUtils;
//import com.imjcker.api.common.util.RedisKeyUtil;
//import com.imjcker.api.common.util.RedisUtil;
//import com.imjcker.gateway.mapper.AuthMapper;
//import com.imjcker.gateway.mapper.BillingRulesMapper;
//import com.imjcker.gateway.mapper.CustomerMapper;
//import com.imjcker.gateway.po.BillingRules;
//import com.imjcker.gateway.po.CompanyApps;
//import com.imjcker.gateway.po.CompanyAppsAuth;
//import com.imjcker.gateway.service.ChargeService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.JedisCluster;
//
//import java.util.List;
//
///**
// * @Author WT
// * @Date 11:34 2020/3/11
// * @Version ChargeServiceImpl v1.0
// * @Desicrption
// */
//@Service
//public class ChargeServiceImpl implements ChargeService {
//
//    @Autowired
//    private CustomerMapper customerMapper;
//
//    @Autowired
//    private BillingRulesMapper billingRulesMapper;
//
//    @Autowired
//    private AuthMapper authMapper;
//
//    private static final Logger logger = LoggerFactory.getLogger(ChargeServiceImpl.class);
//
//    // 校验余额及计费
//    @Override
//    public boolean chargePrice(String appKey, Integer apiId) {
//        // 查询是否是客户计费方式还是接口计费方式
//        CompanyApps companyApps = findCompanyAppsVoByAppKey(appKey);
//        String today = DateUtil.getTodayDate();
//        String operatorKey;
//        String operatorValue;
//        String initValue;
//        if (StringUtils.isNotBlank(companyApps.getStrategyUuid())) {
//            // 采用客户级别扣费
//            BillingRules billingRules = findBillingRulesByUuid(companyApps.getStrategyUuid());
//
//            if (billingRules.getBillingCycleLimit() != null) {
//                if (billingRules.getBillingCycleLimit().intValue() == -1) {
//                    //logger.info("客户计费包时不限量");
//                    return true;
//                }
//                // 采用调用次数计费
//                operatorKey = RedisKeyUtil.getCustomerLimitKey(appKey);
//                operatorValue = "1";
//                initValue = billingRules.getBillingCycleLimit().toString();
//            } else {
//                // 采用 余额计费
//                operatorKey = RedisKeyUtil.getCustomerBalanceKey(appKey);
//                operatorValue = companyApps.getPrice().toString();
//                initValue = companyApps.getBalance().toString();
//            }
//        } else {
//            // 按接口合约计费
//            CompanyAppsAuth companyAppsAuth = findCompanyAppsAuth(appKey, apiId);
//            BillingRules billingRules = findBillingRulesByUuid(companyAppsAuth.getStrategyUuid());
//            if (billingRules.getBillingCycleLimit() != null) {
//                if (billingRules.getBillingCycleLimit().intValue() == -1) {
//                    //logger.info("接口合约包时不限量");
//                    return true;
//                }
//                // 采用调用次数计费
//                operatorKey = RedisKeyUtil.getAuthLimitKey(appKey, apiId);
//                operatorValue = "1";
//                initValue = billingRules.getBillingCycleLimit().toString();
//            } else {
//                // 按条收费
//                operatorKey = RedisKeyUtil.getCustomerBalanceKey(appKey);
//                operatorValue = companyAppsAuth.getPrice().toString();
//                initValue = companyApps.getBalance().toString();
//            }
//        }
//        // 执行扣减操作
//        String realKey = RedisKeyUtil.concatRedisKeyDate(operatorKey, today);
//        int decrStatus = operateRemained(realKey, operatorValue);
//        if (decrStatus == 0) {
//            //logger.info("客户余额扣减成功,appKey: {}, apiId: {}", appKey, apiId);
//            return true;
//        } else if (decrStatus == 1) {
//            logger.debug("客户余额不足,appKey: {}, 当前余额: {}, 价格为: {}",
//                    appKey, RedisUtil.get(realKey, String.class), operatorValue);
//            return false;
//        }
//        // redis中不存在余额 key ,进行初始化
//        String yesterday = DateUtil.getYesterdayDate();
//        initCustomerBalance(operatorKey, today, yesterday, initValue);
//        // 再次扣除
//        decrStatus = operateRemained(realKey, operatorValue);
//        if (decrStatus == 0) {
//            logger.debug("客户余额扣减成功,appKey: {}, apiId: {}", appKey, apiId);
//            return true;
//        } else {
//            logger.debug("客户余额不足,appKey: {}, 当前余额: {}, 价格为: {}",
//                    appKey, RedisUtil.get(realKey, String.class), operatorValue);
//            return false;
//        }
//    }
//
//    /**
//     * @param operatorKey
//     * @param initValue
//     * @Description : 初始化库存余额的redis lua脚本
//     * @Return : void
//     * @Date : 2020/3/23 10:23
//     */
//    private void initCustomerBalance(String operatorKey, String today, String yesterday, String initValue) {
//        /*String initScripts = "local operator_key = KEYS[1] \n" +
//                "local init_value = ARGV[1] \n" +
//                "local is_exists = redis.call(\"EXISTS\", operator_key) \n" +
//                "if is_exists == 1 then \n" +
//                "\treturn 0\n" +
//                "else \n" +
//                "\tredis.call(\"SET\",operator_key, init_value) \n" +
//                "\treturn 1 \n" +
//                "end \t";*/
//        String todayKey = RedisKeyUtil.concatRedisKeyDate(operatorKey, today);
//        String yesterdayKey = RedisKeyUtil.concatRedisKeyDate(operatorKey, yesterday);
//        String initScripts = "local today_key = KEYS[1] \n" +
//                "local yesterday_key = KEYS[2] \n" +
//                "local init_value = ARGV[1] \n" +
//                "local today_exists = redis.call(\"EXISTS\", today_key) \n" +
//                "if today_exists == 1 then \n" +
//                "\treturn 0\n" +
//                "else \n" +
//                "\tlocal yesterday_exists = redis.call(\"EXISTS\", yesterday_key) \n" +
//                "\tif yesterday_exists == 1 then \n" +
//                "\t\tlocal yesterday_value = redis.call(\"GET\", yesterday_key) \n" +
//                "\t\tredis.call(\"SET\", today_key, yesterday_value) \n" +
//                "\telse \n" +
//                "\t\tredis.call(\"SET\", today_key, init_value) \n" +
//                "\tend \n" +
//                "\treturn 0\n" +
//                "end ";
//        JedisCluster jedis = RedisClusterUtils.getJedis();
//        jedis.eval(initScripts, 2, todayKey, yesterdayKey, initValue);
//    }
//
//    /**
//     * @param operatorKey
//     * @param operatorValue
//     * @Description : 扣除余量的redis lua脚本, 对传入的operatorKey对应的值扣去传入的值operatorValue
//     * 扣减余额,operatorValue对应的是单价, 扣减调用次数,operatorValue对应为1,即减 1 次
//     * @Return : java.lang.Integer  0 成功   1 余额不足  -1 不存在key
//     * @Date : 2020/3/23 9:21
//     */
//    private Integer operateRemained(String operatorKey, String operatorValue) {
//        String decrScripts = "local operator_key = KEYS[1] \n" +
//                "local operator_value = ARGV[1] \n" +
//                "local is_exists = redis.call(\"EXISTS\", operator_key) \n" +
//                "if is_exists == 1 then \n" +
//                "\tlocal remain_value = redis.call(\"GET\",operator_key) \n" +
//                "\tlocal changed_value = remain_value - operator_value \n" +
//                "\tif changed_value < 0 then \n" +
//                "\t\treturn 1 \n" +
//                "\telse \n" +
//                "\t\tredis.call(\"SET\",operator_key,changed_value) \n" +
//                "\t\treturn 0 \n" +
//                "\tend \n" +
//                "else \n" +
//                "\treturn -1 \n" +
//                "end ";
//        JedisCluster jedis = RedisClusterUtils.getJedis();
//        Object eval = jedis.eval(decrScripts, 1, operatorKey, operatorValue);
//
//        return Integer.parseInt(eval.toString());
//    }
//
//    /**
//     * @param appKey
//     * @param apiId
//     * @Description : 获取合约信息
//     * @Return : com.lemon.zuul.logic.po.CompanyAppsAuth
//     * @Date : 2020/4/17 16:31
//     */
//    @Override
//    public CompanyAppsAuth findCompanyAppsAuth(String appKey, Integer apiId) {
//        String authKey = RedisKeyUtil.getAuthInfoKey(appKey, apiId);
//        JSONObject authJson = RedisUtil.get(authKey);
//        CompanyAppsAuth companyAppsAuth = null;
//        if (null != authJson) {
//            companyAppsAuth = authJson.toJavaObject(CompanyAppsAuth.class);
//        } else {
//            List<CompanyAppsAuth> list = authMapper.findAppKeyAndApiId(appKey, apiId);
//            if (list == null || list.size() <= 0) {
//                return null;
//            }
//            companyAppsAuth = list.get(0);
//            RedisUtil.setToCaches(authKey, companyAppsAuth);
//        }
//        return companyAppsAuth;
//    }
//
//    /**
//     * @param strategyUUid
//     * @Description : 获取计费规则
//     * @Return : com.lemon.zuul.logic.po.BillingRules
//     * @Date : 2020/4/17 16:31
//     */
//    @Override
//    public BillingRules findBillingRulesByUuid(String strategyUUid) {
//        String chargeRuleKey = RedisKeyUtil.getChargeRuleKey(strategyUUid);
//        JSONObject chargeBillRule = RedisUtil.get(chargeRuleKey);
//        BillingRules billingRules = null;
//        if (null != chargeBillRule) {
//            billingRules = chargeBillRule.toJavaObject(BillingRules.class);
//        } else {
//            billingRules = billingRulesMapper.selectByBillingRulesUuid(strategyUUid);
//            RedisUtil.setToCaches(chargeRuleKey, billingRules);
//        }
//        return billingRules;
//    }
//
//    /**
//     * @param appKey
//     * @Description : 获取客户信息
//     * @Return : com.lemon.zuul.logic.po.CompanyApps
//     * @Date : 2020/4/17 16:30
//     */
//    @Override
//    public CompanyApps findCompanyAppsVoByAppKey(String appKey) {
//        String customerKey = RedisKeyUtil.getCustomerInfoKey(appKey);
//        JSONObject jsonObject = RedisUtil.get(customerKey);
//        CompanyApps companyApps = null;
//        if (null != jsonObject) {
//            companyApps = jsonObject.toJavaObject(CompanyApps.class);
//        } else {
//            companyApps = customerMapper.findCustomerByAppKey(appKey);
//            RedisUtil.setToCaches(customerKey, companyApps);
//        }
//        return companyApps;
//    }
//
//    /**
//     * @param appKey
//     * @param apiId
//     * @Description : 客户退款操作
//     * @Return : void
//     * @Date : 2020/3/18 9:12
//     */
//    @Override
//    public void refund(String appKey, Integer apiId) {
//        String operatorKey;
//        String operatorValue;
//        CompanyApps companyApps = findCompanyAppsVoByAppKey(appKey);
//        String today = DateUtil.getTodayDate();
//        if (StringUtils.isNotBlank(companyApps.getStrategyUuid())) {
//            // 客户级别退款
//            BillingRules billingRules = findBillingRulesByUuid(companyApps.getStrategyUuid());
//            if (billingRules.getBillingCycleLimit() != null) {
//                // 调用次数方式
//                operatorKey = RedisKeyUtil.getCustomerLimitKeyByDate(appKey, today);
//                operatorValue = "-1";
//            } else {
//                // 余额方式退款
//                operatorKey = RedisKeyUtil.getCustomerBalanceKeyByDate(appKey, today);
//                // 取价格负数
//                operatorValue = companyApps.getPrice().negate().toString();
//            }
//        } else {
//            //接口级别
//            CompanyAppsAuth companyAppsAuth = findCompanyAppsAuth(appKey, apiId);
//            BillingRules billingRulesByUuid = findBillingRulesByUuid(companyAppsAuth.getStrategyUuid());
//            if (billingRulesByUuid.getBillingCycleLimit() != null) {
//                // 采用调用次数计费
//                operatorKey = RedisKeyUtil.getAuthLimitKeyByDate(appKey, apiId, today);
//                operatorValue = "-1";
//            } else {
//                // 按条收费
//                operatorKey = RedisKeyUtil.getCustomerBalanceKeyByDate(appKey, today);
//                operatorValue = companyAppsAuth.getPrice().negate().toString();
//            }
//        }
//        // 进行余量操作
//        Integer refundStatus = operateRemained(operatorKey, operatorValue);
//        if (0 == refundStatus)
//            logger.debug("退款成功,appKey: {}, apiId: {}", appKey, apiId);
//        else
//            logger.debug("退款失败,appKey: {}, apiId: {}", appKey, apiId);
//    }
//
//}
