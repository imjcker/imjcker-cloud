package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.po.RateLimitAppkey;
import com.imjcker.manager.manage.po.RateLimitAppkeyApi;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyApiQuery;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyQuery;

public interface RateLimitService {

    /**
     * search appKey
     * @param query appKey
     * @return list of RateLimitAppkey
     */
    void searchAppKey(RateLimitAppKeyQuery query);
    /**
     * search appKey
     * @param query appKey
     * @return list of RateLimitAppkey
     */
    void searchAppKeyApi(RateLimitAppKeyApiQuery query);

    RateLimitAppkey appKeyBindingStrategy(RateLimitAppkey rateLimitAppkey);

    RateLimitAppkeyApi appKeyApiBindingStrategy(RateLimitAppkeyApi rateLimitAppkeyApi);

    RateLimitAppkey getAppKey(String appKey);

    RateLimitAppkey saveAppKey(RateLimitAppkey rateLimitAppkey);

    RateLimitAppkeyApi getAppKeyApi(String appKey, int apiId);

    RateLimitAppkeyApi saveAppKeyApi(RateLimitAppkeyApi rateLimitAppkeyApi);
}
