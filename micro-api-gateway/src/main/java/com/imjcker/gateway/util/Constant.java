package com.imjcker.gateway.util;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 常量定义
 * @Package com.lemon.zuul.util
 * @date 2017年7月12日 上午9:55:04
 */
public interface Constant {
    //发布版本
    Integer IS_CURRENT_VERSION = 1;
    //非发布版本
    Integer NOT_CURRENT_VERSION = 2;
    //线上
    Integer ENV_ONLINE = 1;
    //测试
    Integer ENV_TEST = 2;

    //包量计费阀值
    Integer PACKGE_CHARGE_THRESHOLD = 500;

    //参数类型, 1:代表String,2:int,3:long,4:float,5:double,6:boolean
    Integer STRING_TYPE = 1;
    Integer INT_TYPE = 2;
    Integer LONG_TYPE = 3;
    Integer FLOAT_TYPE = 4;
    Integer DOUBLE_TYPE = 5;
    Integer BOOLEAN_TYPE = 6;

    String PARAM_KEY_ORDER_ID = "zuul_requestUuid";            //请求参数orderId的key
    String PARAM_KEY_API_VERSION_ID = "zuul_versionId";        //请求参数versionId的key
    String PARAM_KEY_APP_KEY = "appkey";                    //请求参数appKey的key

    //必填参数
    Integer PARAM_IS_MUST = 1;
    //非必填参数
    Integer PARAM_NOT_MUST = 2;
    //使用mock数据
    Integer IS_MOCK = 1;
    //不使用mock数据
    Integer NOT_MOCK = 2;

    //限流常量
    /*
    rl_xx_total 用于动态记录appKey/api或者其组合的请求次数, 用于跟策略中的总量比较大小;
    rl_xx       用于动态记录appKey/api或者其组合的请求次数, 用于跟策略中的频率比较大小.
     */
    String RL_APPKEY_TOTAL = "rl_appKey_total:"; //appKey 对应的总量
    String RL_APPKEY_RATE = "rl_appKey:";        //appKey 对应的频率记录
    String RL_API_TOTAL = "rl_api_total:";       //api 总量
    String RL_API_RATE = "rl_api:";              //api 频率
    String RL_APPKEY_API_TOTAL = "rl_appKey_api_total:"; //appKey + api 总量
    String RL_APPKEY_API_RATE = "rl_appKey_api:";        //appKey + api 频率

}
