package com.imjcker.spring.cloud.service.mail.controller;

import com.imjcker.spring.cloud.service.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

/**
 * @author thh 2018-10-23
 * @version 1.0.0
 * description:
 **/
@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("send")
    public String sendEmail(@RequestParam String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("1335111211@qq.com");
        mailMessage.setTo("570577029@qq.com");
        mailMessage.setText(message);
        try {
            return mailService.sendMail(mailMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
