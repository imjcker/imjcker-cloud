package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.CommonResult;
import com.imjcker.api.handler.po.RespCompanyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MicroChargeServiceImpl implements MicroChargeService {
    private static final Logger log = LoggerFactory.getLogger(MicroChargeServiceImpl.class);

    @Override
    public CommonResult<RespCompanyKey> selectKey(String appKey) {
        return null;
    }

    @Override
    public String getChargeByApiId(JSONObject object) {
        log.error("Exit 请求 Charge 超时，执行服务降级。");
        return CommonResult.success("{\"data\":\"Exit 请求 Charge 超时，执行服务降级。\"}").toString();
    }

    @Override
    public CommonResult selectByUuid(String uuid) {
        log.error("Exit 请求 Charge 超时，执行服务降级。");
        return CommonResult.success("{\"data\":\"Exit 请求 Charge 超时，执行服务降级。\"}");
    }
}
