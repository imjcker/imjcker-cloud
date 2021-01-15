package com.imjcker.manager.manage.vo;

public class AutoTestResponse {

    private Integer id;

    private Integer apiId;

    private Integer requestParamsId;

    private String paramName;

    private String paramValue;

    private Integer paramsLocation;

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

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Integer getParamsLocation() {
        return paramsLocation;
    }

    public void setParamsLocation(Integer paramsLocation) {
        this.paramsLocation = paramsLocation;
    }
}
