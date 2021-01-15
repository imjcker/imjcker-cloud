package com.imjcker.gateway.po;

import lombok.Data;

/**
 * @Author WT
 * @Date 10:45 2020/2/24
 * @Version CompanyAppsVo v1.0
 * @Desicrption 客户
 */
@Data
public class CompanyAppsVo extends CompanyApps {

    private String strategyName;

    @Override
    public String toString() {
        return "CompanyAppsVo{" +
                "strategyName='" + strategyName + '\'' +
                "} " + super.toString();
    }
}
