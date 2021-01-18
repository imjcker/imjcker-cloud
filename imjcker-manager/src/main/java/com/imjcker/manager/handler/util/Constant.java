package com.imjcker.api.handler.util;

/**
 * 系统常量
 */
public interface Constant {

    String REDIS_KEY_PRE = "gateway:client:";            //存入Redis的key前缀

    String REDIS_KEY_RESULT = "result:";            //存入Redis的key前缀

    String PARAM_KEY_ORDER_ID = "requestUuid";            //请求参数orderId的key
    String PARAM_KEY_API_VERSION_ID = "versionId";        //请求参数versionId的key
    String PARAM_KEY_APP_KEY = "appkey";                    //请求参数appKey的key


    //redis key处理常量
    String KEY_LIST_SEPERATOR = ",";
    String SUBKEY_SEPERATOR = "\\.";
    String KEY_LIST = "redis_key_list";
    String KEY_POSITION = "redis_key_position";

    String PARAM_TRANS_CODE = "0";//header参数，转码标示
}
