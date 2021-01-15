package com.imjcker.spring.cloud.service.mail.service;


import org.springframework.mail.SimpleMailMessage;

/**
 * 邮件服务
 */
public interface MailService {
    /**
     * 发送邮件
     * @param message com.imjcker.management.message
     * @throws Exception exception
     */
    String sendMail(SimpleMailMessage message)throws Exception;
}
