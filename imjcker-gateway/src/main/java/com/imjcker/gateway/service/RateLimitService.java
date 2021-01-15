package com.imjcker.gateway.service;

import com.imjcker.gateway.po.RateLimitAppkey;
import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.po.RateLimitAppkeyApi;

import javax.servlet.http.HttpServletRequest;

public interface RateLimitService {
    RateLimitAppkey getAppKey(String appKey);

    RateLimitAppkey saveAppKey(RateLimitAppkey rateLimitAppkey);

    RateLimitAppkeyApi getAppKeyApi(String appKey, int apiId);

    RateLimitAppkeyApi saveAppKeyApi(RateLimitAppkeyApi rateLimitAppkeyApi);

    ApiInfoVersions getApiInfo(long apiId, HttpServletRequest request);
}
