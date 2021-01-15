package com.imjcker.manager.charge.po;

import lombok.Data;

@Data
public class CompanyAppsAuthVo extends CompanyAppsAuth{

    private String strategyName;

    /**
     * 接口平常
     */
    private String apiName;

    @Override
    public String toString() {
        return "CompanyAppsAuthVo{" +
                "strategyName='" + strategyName + '\'' +
                "} " + super.toString();
    }
}
