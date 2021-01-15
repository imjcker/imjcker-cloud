package com.imjcker.api.common.vo;

public class MonitorMsgPoJo {
	//请求流水号
	private String requestUuid;
	//请求开始时间戳
	private Long requestTime;
	//请求结束时间戳
	private Long responseTime;
	//请求耗时
	private Long spendTime;
	//请求url
	private String url;
	//请求真实IP
	private String ipv4;
	//versionId
	private String versionId;
	//appkey
	private String appkey;
	//apiId
	private Integer apiId;
	private String apiName;
	//请求参数
	private Object requestParams;
	//返回信息
	private Object resultMsg;
	//返回状态码
	private int errorCode;
	//计费相关字段
	//计费规则uuid
	private String billingRulesUuid;
	//单价
	private double price;

	public String getBillingRulesUuid() {
		return billingRulesUuid;
	}

	public void setBillingRulesUuid(String billingRulesUuid) {
		this.billingRulesUuid = billingRulesUuid;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getRequestUuid() {
		return requestUuid;
	}
	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	public Object getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(Object requestParams) {
		this.requestParams = requestParams;
	}
	public Object getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(Object resultMsg) {
		this.resultMsg = resultMsg;
	}
	public Long getSpendTime() {
		return spendTime;
	}
	public void setSpendTime(Long spendTime) {
		this.spendTime = spendTime;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
