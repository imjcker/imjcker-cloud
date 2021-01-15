//package com.imjcker.manager.manage.po;
//
//import org.springframework.data.elasticsearch.annotations.Document;
//
//@Document(indexName="gateway_system",type="monitor")
//public class MonitorEsPoJo {
//	//请求流水号
//	private String requestUuid;
//	//请求开始时间戳
//	private Long requestTime;
//	//请求结束时间戳
//	private Long responseTime;
//	//请求耗时
//	private Long spendTime;
//	//请求url
//	private String url;
//	//请求真实IP
//	private String ip;
//	//versionId
//	private String versionId;
//	//appkey
//	private String appkey;
//	//apiId
//	private Integer apiId;
//	//请求参数
//	private Object requestParams;
//	//返回信息
//	private Object resultMsg;
//	//返回状态码
//	private int erroCode;
//
//	public String getRequestUuid() {
//		return requestUuid;
//	}
//	public void setRequestUuid(String requestUuid) {
//		this.requestUuid = requestUuid;
//	}
//	public Long getRequestTime() {
//		return requestTime;
//	}
//	public void setRequestTime(Long requestTime) {
//		this.requestTime = requestTime;
//	}
//	public Long getResponseTime() {
//		return responseTime;
//	}
//	public void setResponseTime(Long responseTime) {
//		this.responseTime = responseTime;
//	}
//	public String getUrl() {
//		return url;
//	}
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	public String getIp() {
//		return ip;
//	}
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//	public String getVersionId() {
//		return versionId;
//	}
//	public void setVersionId(String versionId) {
//		this.versionId = versionId;
//	}
//	public String getAppkey() {
//		return appkey;
//	}
//	public void setAppkey(String appkey) {
//		this.appkey = appkey;
//	}
//	public Integer getApiId() {
//		return apiId;
//	}
//	public void setApiId(Integer apiId) {
//		this.apiId = apiId;
//	}
//	public Object getRequestParams() {
//		return requestParams;
//	}
//	public void setRequestParams(Object requestParams) {
//		this.requestParams = requestParams;
//	}
//	public Object getResultMsg() {
//		return resultMsg;
//	}
//	public void setResultMsg(Object resultMsg) {
//		this.resultMsg = resultMsg;
//	}
//	public Long getSpendTime() {
//		return spendTime;
//	}
//	public void setSpendTime(Long spendTime) {
//		this.spendTime = spendTime;
//	}
//	public int getErroCode() {
//		return erroCode;
//	}
//	public void setErroCode(int erroCode) {
//		this.erroCode = erroCode;
//	}
//}
