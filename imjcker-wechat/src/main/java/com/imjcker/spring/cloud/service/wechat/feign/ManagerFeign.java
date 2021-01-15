package com.imjcker.spring.cloud.service.wechat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "spring-cloud-service-manager")
public interface ManagerFeign {

    @GetMapping("/manager/sayHi")
    String sayHi();

    @PostMapping("/manager/sendMail")
    String sendMail(@RequestParam String message, @RequestParam(required = false) Integer id);
}
