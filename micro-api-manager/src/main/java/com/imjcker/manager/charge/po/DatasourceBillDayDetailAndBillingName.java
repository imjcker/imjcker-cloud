package com.imjcker.manager.charge.po;

public class DatasourceBillDayDetailAndBillingName extends DatasourceBillDayDetail{

    private String strategyName;//计费规则名称

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
