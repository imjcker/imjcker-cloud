package com.imjcker.api.handler.service;



import com.imjcker.api.handler.model.MainEntity;

import java.util.Map;


public interface RequestService {
    String doRequest(MainEntity mainEntity, MainEntity.Params params, String appKey, String redisParams, String retryFlag, int retryCount, Map<String, Object> map) throws Exception;
}
