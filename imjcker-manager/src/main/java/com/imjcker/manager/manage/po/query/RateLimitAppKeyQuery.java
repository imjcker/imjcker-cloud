package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.vo.RateLimitAppKeyVO;

public class RateLimitAppKeyQuery extends PageQuery<RateLimitAppKeyVO> {
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
