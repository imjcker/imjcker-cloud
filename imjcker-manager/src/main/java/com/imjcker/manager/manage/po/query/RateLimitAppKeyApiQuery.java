package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.vo.RateLimitAppKeyApiVO;

public class RateLimitAppKeyApiQuery  extends PageQuery<RateLimitAppKeyApiVO>{
    private String appKey;
    private Integer apiId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }
}
