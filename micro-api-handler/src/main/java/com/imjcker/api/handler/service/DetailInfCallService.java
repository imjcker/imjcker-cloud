package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.ApiCombinationInfo;
import com.imjcker.api.handler.model.ApiCombinationInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author WT
 * @Date 16:28 2019/8/20
 * @Version DetailInfCallService v1.0
 * @Desicrption
 */
public interface DetailInfCallService {
    Map<String, String> callRequest(List<ApiCombinationInfo> sortList, JSONObject json) throws Exception;
}
