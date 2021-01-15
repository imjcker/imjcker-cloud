package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.service.WebBackendService;
import com.lemon.common.vo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class WebBackendServiceImpl implements WebBackendService {
    private static final Logger log = LoggerFactory.getLogger(WebBackendServiceImpl.class);
    @Override
    public String getGroupNameByApiId(JSONObject object) {
        log.error("Charge 请求 Backend 超时，执行服务降级。");
        return JSONObject.toJSONString(CommonResult.success("{\"data\":\"Charge 请求 Backend 超时，执行服务降级。\"}"));
    }

    @Override
    public String getApiNameByApiId(JSONObject object) {
        log.error("Charge 请求 Backend 超时，执行服务降级。");
        return JSONObject.toJSONString(CommonResult.success("{\"data\":\"Charge 请求 Backend 超时，执行服务降级。\"}"));
    }
}
