package com.imjcker.spring.cloud.service.wechat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "spring-cloud-service-mail")
public interface MailFeign {
    @PostMapping("/mail/send")
    String sendMail(@RequestParam String message);
}
