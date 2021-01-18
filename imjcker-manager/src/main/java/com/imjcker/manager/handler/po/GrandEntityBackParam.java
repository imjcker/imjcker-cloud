package com.imjcker.api.handler.po;

/**
 * 父接口，子接口，共有属性
 */
public class GrandEntityBackParam {

    private Integer paramsType;
    private String paramValue;
    private Integer paramsLocation;
    private String paramName;
    private Integer requestParamsId;

    public Integer getParamsType() {
        return paramsType;
    }

    public void setParamsType(Integer paramsType) {
        this.paramsType = paramsType;
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

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getRequestParamsId() {
        return requestParamsId;
    }

    public void setRequestParamsId(Integer requestParamsId) {
        this.requestParamsId = requestParamsId;
    }
}
