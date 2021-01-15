package com.imjcker.manager.manage.po;

public class BackendRequestParams {
    private Integer id;

    private Integer apiId;

    private Integer requestParamsId;

    private Integer paramsType;

    private String paramName;

    private String paramValue;

    private Integer paramsLocation;

    private String paramDescription;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getRequestParamsId() {
        return requestParamsId;
    }

    public void setRequestParamsId(Integer requestParamsId) {
        this.requestParamsId = requestParamsId;
    }

    public Integer getParamsType() {
        return paramsType;
    }

    public void setParamsType(Integer paramsType) {
        this.paramsType = paramsType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue == null ? null : paramValue.trim();
    }

    public Integer getParamsLocation() {
        return paramsLocation;
    }

    public void setParamsLocation(Integer paramsLocation) {
        this.paramsLocation = paramsLocation;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription == null ? null : paramDescription.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
