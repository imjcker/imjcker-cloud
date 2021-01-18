package com.imjcker.api.handler.model;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.plugin.resourceCache.IJsonSerializer;

import java.util.Map;

/**
 * 保存的Model,用于存MongoDB和发送MQ
 */
public class SaveModel implements IJsonSerializer{
	//流水号
	private String uid;
	//发起请求的时间戳
	private Long orderIdCreateTime;
	//请求url
	private String url;
	//上游接口唯一标识
	private String interfaceUuid;
	//机构标识，appkey
	private String companyTag;
	//调用上游成功与否, 1:成功, 2:失败
	private Integer sourceStatus;
	//下游调用我们是否成功 1:成功, 2:失败
	private Integer targetStatus;
	//是否使用缓存,1不使用,2使用
	private Integer cached;
	//给第三方传的请求参数
	private Object params;
	//原始请求参数
	private Map initParams;
	//调用三方返回的结果，是json字符串
	private String initResult;
	//我们给客户返回的真实结果
	private ThirdResult trueResult;

	public SaveModel() {
		super();
	}

	public SaveModel(String uid, Object params, String initResult, ThirdResult trueResult) {
		super();
		this.uid = uid;
		this.params = params;
		this.initResult = initResult;
		this.trueResult = trueResult;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public String getInitResult() {
		return initResult;
	}

	public void setInitResult(String initResult) {
		this.initResult = initResult;
	}

	public ThirdResult getTrueResult() {
		return trueResult;
	}

	public void setTrueResult(ThirdResult trueResult) {
		this.trueResult = trueResult;
	}

	public Long getOrderIdCreateTime() {
		return orderIdCreateTime;
	}

	public void setOrderIdCreateTime(Long orderIdCreateTime) {
		this.orderIdCreateTime = orderIdCreateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInterfaceUuid() {
		return interfaceUuid;
	}

	public void setInterfaceUuid(String interfaceUuid) {
		this.interfaceUuid = interfaceUuid;
	}

	public String getCompanyTag() {
		return companyTag;
	}

	public void setCompanyTag(String companyTag) {
		this.companyTag = companyTag;
	}

	public Integer getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(Integer sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public Integer getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(Integer targetStatus) {
		this.targetStatus = targetStatus;
	}

	public Integer getCached() {
		return cached;
	}

	public void setCached(Integer cached) {
		this.cached = cached;
	}

	public Map getInitParams() {
		return initParams;
	}

	public void setInitParams(Map initParams) {
		this.initParams = initParams;
	}

	@Override
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

}
