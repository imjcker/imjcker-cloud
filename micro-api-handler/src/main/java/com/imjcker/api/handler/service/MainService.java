package com.imjcker.api.handler.service;



import com.imjcker.api.handler.po.*;

import java.util.List;
import java.util.Map;

public interface MainService {
    /*
     * 前后端接口信息
     */
    ApiInfoVersionsWithBLOBs selectApiInfo(String versionId);

    /*
     * 前端请求参数
     */
    List<RequestParamsVersions> selectRequestParams(String versionId);

    /*
     * 后端请求参数
     */
    List<BackendRequestParamsVersions> selectBackParams(String versionId);

    ApiInfoResponse findApiInfoById(Integer id);

    List<BackendParamsResponse> selectCustomBackendParam(Map<String, Object> map);
}
