package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.ApiInfoResponse;
import com.imjcker.api.handler.po.ApiInfoVersionsWithBLOBs;
import com.imjcker.api.handler.po.BackendRequestParamsVersions;
import com.imjcker.api.handler.po.RequestParamsVersions;


import java.util.List;
import java.util.Map;

/**
 * @Author WT
 * @Date 16:47 2019/8/20
 * @Version ParamsService v1.0
 * @Desicrption
 */
public interface ParamsService {
    ApiInfoVersionsWithBLOBs selectApiInfo(String versionId);

    List<BackendRequestParamsVersions> selectBackParams(String versionId);

    List<RequestParamsVersions> selectRequestParams(String versionId);

    MainEntity.Params handleMainParams(MainEntity mainEntity, JSONObject json, Map<String, Object> accountMap);

    MainEntity getMainEntity(String versionId);

    ApiInfoResponse findApiInfoById(Integer apiId);
}
