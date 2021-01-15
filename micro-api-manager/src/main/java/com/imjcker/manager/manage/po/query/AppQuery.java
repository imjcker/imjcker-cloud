package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.po.App;

/**
 * Created by lilinfeng on 2017/7/12.
 */
public class AppQuery extends PageQuery<App>{
//    private XXX 这里写界面上的查询条件

    private Integer appId;
    private String appName;
    private Integer strategyId;
    private Integer env;
    private Integer apiId;
    private String uuid;
    public Integer getEnv() {
        return env;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

}
