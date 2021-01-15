package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.util.HttpClientProxy;
import com.imjcker.manager.manage.service.MicroServiceCommonService;
import com.imjcker.manager.vo.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MicroServiceCommonServiceImpl implements MicroServiceCommonService {

    public Object invoke(String url, Map<String, String> headers, JSONObject jsonObject) {
        String result = HttpClientProxy.postByJson(url, headers, jsonObject);
        if (StringUtils.contains(url, "debugging")) {
            return result;
        }
        return JSON.parseObject(result, CommonResult.class);
    }

    @Override
    public Object invoke(String url, Map<String, String> headers, byte[] bytes) {
        String result = HttpClientProxy.postBytes(url, headers, bytes);

        return JSON.parseObject(result, CommonResult.class);
    }

}
