package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author WT
 * @Date 16:16 2019/9/20
 * @Version GatewayExitService v1.0
 * @Desicrption
 */
@FeignClient(serviceId = "MICRO-GATEWAY-EXIT-SERVICE", fallback = GatewayExitServiceImpl.class)
public interface GatewayExitService {

    @PostMapping("/exit/general")
    String exitRequest(@RequestBody JSONObject object);
//    String exitRequest( JSONObject object);
}
