package com.imjcker.api.handler.po;

public class ApiRateDistributeWithBLOBs extends ApiRateDistribute {
    private String responseTransParam;

    private String responseConfigJson;

    public String getResponseTransParam() {
        return responseTransParam;
    }

    public void setResponseTransParam(String responseTransParam) {
        this.responseTransParam = responseTransParam == null ? null : responseTransParam.trim();
    }

    public String getResponseConfigJson() {
        return responseConfigJson;
    }

    public void setResponseConfigJson(String responseConfigJson) {
        this.responseConfigJson = responseConfigJson == null ? null : responseConfigJson.trim();
    }
}
