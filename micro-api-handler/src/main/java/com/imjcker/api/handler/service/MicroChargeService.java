package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.CommonResult;
import com.imjcker.api.handler.model.RespBillingRules;
import com.imjcker.api.handler.model.RespBillingRules;
import com.imjcker.api.handler.po.RespCompanyKey;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author WT
 * @Date 15:48 2020/4/16
 * @Version MicroChargeService v1.0
 * @Desicrption  计费管理服务
 */
@FeignClient(serviceId = "MICRO-CHARGE-SERVICE")
public interface MicroChargeService {

    @GetMapping("/auth/getSignKey")
    CommonResult<RespCompanyKey> selectKey(@RequestParam("appKey") String appKey);


    @PostMapping("/datasourceCharge/getChargeByApiId")
    String getChargeByApiId(@RequestBody JSONObject object);

    /** 主键查billingRules对象  */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/billingRules/byUuid",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    CommonResult<RespBillingRules> selectByUuid(@RequestParam("uuid") String uuid);
}
