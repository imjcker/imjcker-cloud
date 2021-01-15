package com.imjcker.manager.charge.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.service.impl.WebBackendServiceImpl;
import com.imjcker.manager.charge.service.impl.WebBackendServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author KJY
 * @Date 16:16 2020/03/31
 * @Version  v1.0
 * @Desicrption
 */
@FeignClient(serviceId = "MICRO-WEB-BACKEND-SERVICE", fallback = WebBackendServiceImpl.class)
public interface WebBackendService {

    @PostMapping("/api/getGroupNameByApiId")
    String getGroupNameByApiId(@RequestBody JSONObject object);

    @PostMapping("/api/getApiNameByApiId")
    String getApiNameByApiId(@RequestBody JSONObject object);
}
