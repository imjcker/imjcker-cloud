package com.imjcker.manager.manage.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface MicroServiceCommonService {

    Object invoke(String url, Map<String, String> headers, JSONObject jsonObject);

    Object invoke(String url, Map<String, String> headers, byte[] bytes);

}
