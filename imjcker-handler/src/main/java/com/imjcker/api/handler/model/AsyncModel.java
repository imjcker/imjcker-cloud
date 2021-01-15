package com.imjcker.api.handler.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;

//import com.lemon.client.plugin.resourceCache.IJsonSerializer;

public class AsyncModel implements IJsonSerializer, Serializable {
    private static final long serialVersionUID = -6699100841777106216L;
    //流水号
    private String uid;
    //父接口uid
    private String parentUid;
    //发起请求的时间戳
    private String orderIdCreateTime;
    //请求url
    private String url;
    //api 主键
    private Long apiId;
    //上游接口唯一标识
    private String interfaceUuid;
    //机构标识，appkey
    private String companyTag;
    //调用上游成功与否, 1:成功, 2:失败
    private Integer sourceStatus;
    //下游调用我们是否成功 1:成功, 2:失败
    private Integer targetStatus;
    //是否调用缓存，1是2否
    private Integer cached;
    //原始请求参数  oriReqParams 或    initParams
    private Map initParams;
    //请求参数
    private Object params;
    //调用三方返回的结果，是json字符串
    private String initResult;
    //我们给客户返回的真实结果
    private JSONObject trueResult;

    public AsyncModel() {
        super();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
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

    public JSONObject getTrueResult() {
        return trueResult;
    }

    public void setTrueResult(JSONObject trueResult) {
        this.trueResult = trueResult;
    }

    public String getOrderIdCreateTime() {
        return orderIdCreateTime;
    }

    public void setOrderIdCreateTime(String orderIdCreateTime) {
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

    public String getParentUid() {
        return parentUid;
    }

    public void setParentUid(String parentUid) {
        this.parentUid = parentUid;
    }

    @Override
    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
