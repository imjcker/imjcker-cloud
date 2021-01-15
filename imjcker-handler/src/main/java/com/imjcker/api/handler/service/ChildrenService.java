package com.imjcker.api.handler.service;


import com.imjcker.api.handler.po.ApiRateDistributeWithBLOBs;
import com.imjcker.api.handler.po.BackendDistributeParams;
import com.imjcker.api.handler.po.RequestParamsVersions;

import java.util.List;

public interface ChildrenService {
    /*
     * 后端接口信息
     */
    ApiRateDistributeWithBLOBs childApi(String uniqueUuid);

    /*
     * 前端请求参数
     */
    List<RequestParamsVersions> requestParams(String versionId);

    /*
     * 子接口后端请求参数
     */
    List<BackendDistributeParams> childParams(Integer disId);
}
