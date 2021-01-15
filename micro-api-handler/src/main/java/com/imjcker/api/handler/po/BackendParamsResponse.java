package com.imjcker.api.handler.po;

public class BackendParamsResponse {

    private Integer apiId;

    private String backendParamName;

    private String backendParamValue;

    private String requestParamName;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getBackendParamName() {
        return backendParamName;
    }

    public void setBackendParamName(String backendParamName) {
        this.backendParamName = backendParamName;
    }

    public String getBackendParamValue() {
        return backendParamValue;
    }

    public void setBackendParamValue(String backendParamValue) {
        this.backendParamValue = backendParamValue;
    }

    public String getRequestParamName() {
        return requestParamName;
    }

    public void setRequestParamName(String requestParamName) {
        this.requestParamName = requestParamName;
    }
}
