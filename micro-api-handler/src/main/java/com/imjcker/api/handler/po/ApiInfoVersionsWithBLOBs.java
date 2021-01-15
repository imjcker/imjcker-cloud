package com.imjcker.api.handler.po;

public class ApiInfoVersionsWithBLOBs extends ApiInfoVersions {
    private String apiDescription;

    private String mockData;

    private String callBackSuccessExample;

    private String callBackFailExample;

    private String responseTransParam;

    private String jsonConfig;

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription == null ? null : apiDescription.trim();
    }

    public String getMockData() {
        return mockData;
    }

    public void setMockData(String mockData) {
        this.mockData = mockData == null ? null : mockData.trim();
    }

    public String getCallBackSuccessExample() {
        return callBackSuccessExample;
    }

    public void setCallBackSuccessExample(String callBackSuccessExample) {
        this.callBackSuccessExample = callBackSuccessExample == null ? null : callBackSuccessExample.trim();
    }

    public String getCallBackFailExample() {
        return callBackFailExample;
    }

    public void setCallBackFailExample(String callBackFailExample) {
        this.callBackFailExample = callBackFailExample == null ? null : callBackFailExample.trim();
    }

    public String getResponseTransParam() {
        return responseTransParam;
    }

    public void setResponseTransParam(String responseTransParam) {
        this.responseTransParam = responseTransParam == null ? null : responseTransParam.trim();
    }

    public String getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(String jsonConfig) {
        this.jsonConfig = jsonConfig;
    }
}
