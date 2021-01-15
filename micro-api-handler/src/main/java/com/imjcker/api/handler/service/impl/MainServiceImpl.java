package com.imjcker.api.handler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.api.handler.mapper.ApiInfoVersionsMapper;
import com.imjcker.api.handler.mapper.BackendRequestParamsVersionsMapper;
import com.imjcker.api.handler.mapper.RequestParamsVersionsMapper;
import com.imjcker.api.handler.po.*;
import com.imjcker.api.handler.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MainServiceImpl implements MainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainServiceImpl.class);
    @Autowired
    private ApiInfoVersionsMapper apiInfoVersionsMapper;

    @Autowired
    private RequestParamsVersionsMapper requestParamsVersionsMapper;

    @Autowired
    private BackendRequestParamsVersionsMapper backendRequestParamsVersionsMapper;

    @Override
    public ApiInfoVersionsWithBLOBs selectApiInfo(String versionId) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        return list != null && list.size() != 0 ? list.get(0) : null;
    }

    @Override
    public List<RequestParamsVersions> selectRequestParams(String versionId) {
        RequestParamsVersionsExample example = new RequestParamsVersionsExample();
        RequestParamsVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        return requestParamsVersionsMapper.selectByExample(example);
    }

    @Override
    public List<BackendRequestParamsVersions> selectBackParams(String versionId) {
        BackendRequestParamsVersionsExample example = new BackendRequestParamsVersionsExample();
        BackendRequestParamsVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        return backendRequestParamsVersionsMapper.selectByExample(example);
    }

    @Override
    public ApiInfoResponse findApiInfoById(Integer id) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("id", id);
//		return apiInfoVersionsMapper.findApiInfoById(params);
        ApiInfoResponse apiInfoResponse = new ApiInfoResponse();
        JSONObject jsonObject = RedisUtil.get("api:" + id);
        if (jsonObject != null) {
            LOGGER.debug("redis中存在api:{}信息", id);
            ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
            ApiInfoVersionsWithBLOBs apiInfoVersionsWithBLOBs = apiInfoRedisMsg.getApiInfoWithBLOBs();
            apiInfoResponse.setApiName(apiInfoVersionsWithBLOBs.getApiName());
            apiInfoResponse.setApiId(apiInfoVersionsWithBLOBs.getApiId());
            apiInfoResponse.setJsonConfig(apiInfoVersionsWithBLOBs.getJsonConfig());
        } else {
            apiInfoResponse = apiInfoVersionsMapper.findApiInfoById(id);
            LOGGER.debug("redis中不存在api:{}信息,访问数据库", id);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("apiInfoResponse={}", apiInfoResponse.getJsonConfig());
        }
        return apiInfoResponse;
    }

    @Override
    public List<BackendParamsResponse> selectCustomBackendParam(Map<String, Object> map) {
        return apiInfoVersionsMapper.selectCustomBackendParam(map);
    }

}
