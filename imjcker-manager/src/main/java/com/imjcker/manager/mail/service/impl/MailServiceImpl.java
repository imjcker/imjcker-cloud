package com.imjcker.spring.cloud.service.mail.service.impl;

import com.imjcker.spring.cloud.service.mail.mapper.MailMessageDao;
import com.imjcker.spring.cloud.service.mail.model.MailMessage;
import com.imjcker.spring.cloud.service.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final JavaMailSender mailSender;
    private final MailMessageDao mailMessageDao;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, MailMessageDao mailMessageDao) {
        this.mailSender = mailSender;
        this.mailMessageDao = mailMessageDao;
    }

    @Override
    public String sendMail(SimpleMailMessage message) throws Exception {
        assert message != null : "传入有效的邮件消息对象";

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true);

        msg.setFrom(message.getFrom());
        msg.setTo(message.getTo());
        msg.setSubject("公众号动态记录-" + sdf.format(new Date()));
        msg.setText(message.getText(), true);
        mailSender.send(mimeMessage);
        return "发送邮件成功";
    }
}
