package com.imjcker.spring.cloud.service.scheduler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Value("${imjcker}")
    private String imjcker;


    @GetMapping("sayHi")
    public String hiWechat() {
        log.debug("hello from wechat service");
        return "hello from wechat service";
    }

    @GetMapping("env")
    public String env() {
        return this.imjcker;
    }


}
