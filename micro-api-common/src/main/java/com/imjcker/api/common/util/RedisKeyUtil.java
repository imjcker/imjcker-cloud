package com.imjcker.api.common.util;

/**
 * @Author WT
 * @Date 11:11 2020/4/15
 * @Version RedisKeyUtil v1.0
 * @Desicrption 客户,合约,余额余量, 计费规则,存入redis的key获取
 */
public class RedisKeyUtil {

    /*JedisClusterException: No way to dispatch this command to Redis Cluster because keys have different slots.*/
    // 此异常为操作多个key时报错,因为key不在同一个slots中,解决办法

    // key值前面增加{same-slots},redis集群在存取数据时通过{}里面的值确定在哪个slots,
    // 为了将这些值存放一个slots中,避免lua在操作多个key时报错

    private static final String CUSTOMER_BALANCE = "{same-slots}customer:balance:";

    private static final String CUSTOMER_LIMIT = "{same-slots}customer:limit:";

    private static final String AUTH_LIMIT = "{same-slots}auth:limit:";

    private static final String DATA_UPDATE_QUERY = "datasource_update:";

    /*------------------以 : 连接字符串 ---------------------------------*/

    public static String concatRedisKeyDate(String redisKey, String date) {
        return redisKey + ":" + date;
    }


    /*-------------------客户信息---------------------------------------*/
    public static String getCustomerInfoKey(String appKey) {
        return "customer:" + appKey;
    }



    /*-------------客户余额,余量 appKey-----------------------------------*/

    public static String getCustomerBalanceKey(String appKey) {
        return CUSTOMER_BALANCE + appKey;
    }

    public static String getCustomerBalanceKeyByDate(String appKey,String date) {
        return getCustomerBalanceKey(appKey) + ":" + date;
    }

    public static String getCustomerBalanceKeyToday(String appKey) {
        return getCustomerBalanceKeyByDate(appKey, DateUtil.getTodayDate());
    }

    public static String getCustomerBalanceKeyYesterday(String appKey) {
        return getCustomerBalanceKeyByDate(appKey, DateUtil.getYesterdayDate());
    }

    public static String getCustomerLimitKey(String appKey) {
        return CUSTOMER_LIMIT + appKey;
    }

    public static String getCustomerLimitKeyByDate(String appKey, String date) {
        return getCustomerLimitKey(appKey) + ":" + date;
    }

    public static String getCustomerLimitKeyToday(String appKey) {
        return getCustomerLimitKeyByDate(appKey, DateUtil.getTodayDate());
    }

    public static String getCustomerLimitKeyYesterday(String appKey) {
        return getCustomerLimitKeyByDate(appKey, DateUtil.getYesterdayDate());
    }


    /*------------------合约余量 Auth----------------------------------------*/

    public static String getAuthLimitKey(String appKey, Integer apiId) {
        return AUTH_LIMIT + appKey + ":" + apiId;
    }

    public static String getAuthLimitKeyByDate(String appKey, Integer apiId, String date) {
        return getAuthLimitKey(appKey, apiId) + ":" + date;
    }

    public static String getAuthLimitKeyToday(String appKey, Integer apiId) {
        return getAuthLimitKeyByDate(appKey, apiId, DateUtil.getTodayDate());
    }

    public static String getAuthLimitKeyYesterday(String appKey, Integer apiId) {
        return getAuthLimitKeyByDate(appKey, apiId, DateUtil.getYesterdayDate());
    }


    /*----------------- 合约信息 ----------------------------------*/

    public static String getAuthInfoKey(String appKey, Integer apiId) {
        return "auth:" + appKey + ":" + apiId;
    }



    /*------------------ 计费规则 ----------------------------------*/

    public static String getChargeRuleKey(String uuid) {
        return "chargeRule:" + uuid;
    }



    /*----------------- 上游修改规则的亏损情况查询-----------------*/
    public static String getDataUpdateQuery(Integer id){
        return DATA_UPDATE_QUERY+id;
    }

    /*-----------------------------fink余额余量记录key-----------------------*/
    public static String getFinkHistoryKey(String date,String appKey){
        return "User:"+date+":"+appKey;
    }
}
