package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.RateLimitAppkeyApi;

public class RateLimitAppKeyApiVO extends RateLimitAppkeyApi {
    private String apiName;
    private String strategyName;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
