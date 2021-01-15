package com.imjcker.api.common.vo;

public interface Constant {
	//mongoDB database name
	String MONGODB_DATABASE_NAME = "gateway_system";
	//mongoDB collection name
	String MONGODB_COLLECTION_NAME = "monitor";
	//线上
	Integer ENV_ONLINE = 1;
	//预发
	Integer ENV_TEST = 2;
	//【导出API文档】--文档名称
	String WORKUTILS_API_NAME = "${apiName}";
	//【导出API文档】--返回示例
	String WORKUTILS_RESULT_EXAMPLE = "${resultExample}";

	String GATEWAY_CLIENT_SERVICE_A="A";

	String GATEWAY_CLIENT_SERVICE_B="B";

	String GATEWAY_CLIENT_SERVICE_TEST="test";

	String REDIS_KEY_PRE = "gateway:client:";			//存入Redis的key前缀

	/*
       组合接口标识
     */
	String COMBINATION_TYPE = "isCombinationType";
	String COMBINATION_VALUE = "Y";

	// 清除接口缓存标识
	String API_CACHE = "API_CACHE";

	String REQUEST_VERSION_NOW = "v2";

	String REQUEST_VERSION_OLD = "v1";

}
