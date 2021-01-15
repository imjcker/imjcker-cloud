package com.imjcker.manager.manage.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ApiRiskIndex {

    private Integer riskIndexId;

    private Integer productId;

    private Integer apiId;

    private String riskIndexName;

    private String riskIndexField;

    private String productName;

    public Integer getRiskIndexId() {
        return riskIndexId;
    }

    public void setRiskIndexId(Integer riskIndexId) {
        this.riskIndexId = riskIndexId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getRiskIndexName() {
        return riskIndexName;
    }

    public void setRiskIndexName(String riskIndexName) {
        this.riskIndexName = riskIndexName;
    }

    public String getRiskIndexField() {
        return riskIndexField;
    }

    public void setRiskIndexField(String riskIndexField) {
        this.riskIndexField = riskIndexField;
    }

    public List<String> getIndexFields() {

        return Arrays.asList(StringUtils.split(this.riskIndexField,"."));
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
