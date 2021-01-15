package com.imjcker.api.common.vo;

/**
 * @Title: zuul链中保存的header值，供后续组件使用
 * @Package com.lemon.common.vo
 * @author yezhiyuan
 * @date 2017年9月28日 上午9:44:39
 * @version V2.0
 */
public interface ZuulHeader {

	String PARAM_KEY_ORDER_ID = "zuul_requestUuid";			//请求参数orderId的key
	String PARAM_UID_CREATE_TIME = "zuul_uid_create_time";						//流水号生成时间
	String PARAM_KEY_API_VERSION_ID = "zuul_versionId";		//请求参数versionId的key
	String PARAM_KEY_APP_KEY = "appKey";					//请求参数appKey的key

	String PARAM_KEY_TRANS_CODE = "inmgrCode";					//请求参数inmgrCode的key
	String PARAM_API_ID = "zuul_apiId";
	String PARAM_UNIQUE_UUID = "zuul_uniqueUuid";
	String PARAM_WEIGTH = "zuul_weight";

	String PARAM_AGENCY_API_ACCOUNT = "agency_api_account";		//村镇银行第三方接口账户

	String FLUSH_ZUUL_AGENCY_ACCOUNT = "clean_agency_account";	//刷新ZUUL　的本地缓存

	/**  数据请求版本号，老数据 值为1.0 或者空   2.0表示需要解密 */
	String REQUEST_VERSION = "version";
}
