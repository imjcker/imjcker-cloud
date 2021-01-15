package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.RateLimitAppkey;

public class RateLimitAppKeyVO extends RateLimitAppkey {
    private String strategyName;

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
