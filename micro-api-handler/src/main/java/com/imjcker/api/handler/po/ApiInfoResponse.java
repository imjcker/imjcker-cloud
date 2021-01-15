package com.imjcker.api.handler.po;

/**
 * ApiInfoResponse entity with jsonConfig
 */
public class ApiInfoResponse {

    private Integer apiId;

    private String apiName;

    private String jsonConfig;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(String jsonConfig) {
        this.jsonConfig = jsonConfig;
    }
}
