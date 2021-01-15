package com.imjcker.manager.manage.vo;

public class ApiInfoVersionLatestResponse {

    private Integer id;

    private Integer apiId;

    private String apiName;

    private Integer apiGroupId;

    private String autoTest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAutoTest() {
        return autoTest;
    }

    public void setAutoTest(String autoTest) {
        this.autoTest = autoTest;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getApiGroupId() {
        return apiGroupId;
    }

    public void setApiGroupId(Integer apiGroupId) {
        this.apiGroupId = apiGroupId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
