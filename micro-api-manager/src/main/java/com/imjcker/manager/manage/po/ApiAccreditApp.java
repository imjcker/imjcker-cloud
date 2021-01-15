package com.imjcker.manager.manage.po;

/**
 * Created by lilinfeng on 2017/8/3.
 */
public class ApiAccreditApp {
    private Integer apiId;
    private Integer appId;
    private String apiIds;
    private String appIds;
    private Integer env;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public void setApiIds(String apiIds) {
        this.apiIds = apiIds;
    }

    public String getApiIds() {
        return apiIds;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }
}
