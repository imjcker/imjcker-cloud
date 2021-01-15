package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.po.Api;

/**
 * Created by lilinfeng on 2017/7/12.
 */
public class ApiQuery extends PageQuery<Api>{
//    private XXX 这里写界面上的查询条件
    private Integer appId;

    private Integer env;

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}
