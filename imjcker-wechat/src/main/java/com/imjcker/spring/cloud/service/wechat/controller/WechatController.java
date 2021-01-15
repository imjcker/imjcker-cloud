package com.imjcker.spring.cloud.service.wechat.controller;

import com.imjcker.spring.cloud.service.wechat.feign.MailFeign;
import com.imjcker.spring.cloud.service.wechat.feign.ManagerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Value("${imjcker}")
    private String imjcker;

    @Autowired
    private ManagerFeign managerFeign;
    @Autowired
    private MailFeign mailFeign;

    @GetMapping("sayHi")
    public String hiWechat() {
        log.debug("hello from wechat service");
        return "hello from wechat service";
    }

    @GetMapping("env")
    public String env() {
        return this.imjcker;
    }


    @GetMapping("callManager")
    public String callManager() {
        String s = this.managerFeign.sayHi();
        log.debug(s);
        return s;
    }

    @PostMapping("callMail")
    public String callMail(@RequestParam String message) {
        String s = this.mailFeign.sendMail(message);
        log.debug(s);
        return s;
    }


    @PostMapping("callManagerSendMail")
    public String callManagerSendMail(@RequestParam String message, @RequestParam(required = false)Integer id) {
        String s = this.managerFeign.sendMail(message, id);
        log.debug(s);
        return s;
    }



}
