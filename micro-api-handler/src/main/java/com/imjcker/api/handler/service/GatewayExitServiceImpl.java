package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class GatewayExitServiceImpl implements GatewayExitService {
    private static final Logger log = LoggerFactory.getLogger(GatewayExitServiceImpl.class);
    @Override
    public String exitRequest(JSONObject object) {
        log.error("Gateway 请求 Exit 超时，执行服务降级。");
        return JSONObject.toJSONString(CommonResult.success("{\"data\":\"Gateway 请求 Exit 超时，执行服务降级。\"}"));
    }
}
