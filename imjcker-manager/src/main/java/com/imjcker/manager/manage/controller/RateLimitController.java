package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.po.RateLimitAppkey;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyApiQuery;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyQuery;
import com.lemon.common.vo.CommonResult;
import com.imjcker.manager.manage.po.RateLimitAppkeyApi;
import com.imjcker.manager.manage.service.RateLimitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 限流相关api
 */
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RateLimitService rateLimitService;

    /**
     * appKey限流页面
     * @param jsonObject 入参
     * @return return
     */
    @PostMapping("/appKey/index")
    public CommonResult appKeyIndex(@RequestBody JSONObject jsonObject) {
        RateLimitAppKeyQuery query = jsonObject.toJavaObject(RateLimitAppKeyQuery.class);
        String appKey=query.getAppKey();
        if(StringUtils.isNotBlank(appKey)) query.setAppKey(appKey.replace("_","\\_"));
        rateLimitService.searchAppKey(query);
        return CommonResult.success(query);
    }

    /**
     * appKey 绑定策略
     * @param jsonObject 入参
     * @return return
     */
    @PostMapping("/appKey/bindingStrategy")
    public CommonResult appKeyBindingStrategy(@RequestBody JSONObject jsonObject) {
        RateLimitAppkey rateLimitAppkey = jsonObject.toJavaObject(RateLimitAppkey.class);
        rateLimitAppkey.setId(rateLimitService.getAppKey(rateLimitAppkey.getAppKey()).getId());
        rateLimitService.appKeyBindingStrategy(rateLimitAppkey);
        return CommonResult.success();
    }



    /**
     * appKeyApi限流页面
     * @param jsonObject 入参
     * @return return
     */
    @PostMapping("/appKeyApi/index")
    public CommonResult appKeyApiIndex(@RequestBody JSONObject jsonObject) {
        RateLimitAppKeyApiQuery query = jsonObject.toJavaObject(RateLimitAppKeyApiQuery.class);
        String limitName=query.getAppKey();
        if(StringUtils.isNotBlank(limitName)) query.setAppKey(limitName.replace("_","\\_"));
        rateLimitService.searchAppKeyApi(query);
        return CommonResult.success(query);
    }


    /**
     * appKey 绑定策略
     * @param jsonObject 入参
     * @return return
     */
    @PostMapping("/appKeyApi/bindingStrategy")
    public CommonResult appKeyApiBindingStrategy(@RequestBody JSONObject jsonObject) {
        RateLimitAppkeyApi rateLimitAppkeyApi = jsonObject.toJavaObject(RateLimitAppkeyApi.class);
        rateLimitAppkeyApi.setId(rateLimitService.getAppKeyApi(rateLimitAppkeyApi.getAppKey(), rateLimitAppkeyApi.getApiId()).getId());
        rateLimitService.appKeyApiBindingStrategy(rateLimitAppkeyApi);
        return CommonResult.success();
    }
}
