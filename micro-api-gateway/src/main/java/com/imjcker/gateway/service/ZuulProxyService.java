package com.imjcker.gateway.service;

import com.imjcker.gateway.po.CurrentLimitStrategy;
import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.po.RequestParamsVersions;

import java.util.List;

public interface ZuulProxyService {

    List<ApiInfoVersions> getCurrentApiInfo(String httpPath, Integer env);

    CurrentLimitStrategy getLimitStrategy(String limitStrategyuuid);

    List<RequestParamsVersions> getEntitybyVersionId(String versionId);

    String getHttpPathByApiId(Integer apiId);

    String getDataConfigWithTownBank(String appKey,Integer apiGroupId, Integer apiId);

    void flushAgencyAccountCache(String cacheKey);
}
