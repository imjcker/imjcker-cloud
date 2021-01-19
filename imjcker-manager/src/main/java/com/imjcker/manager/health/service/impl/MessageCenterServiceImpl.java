package com.imjcker.manager.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.manager.config.MessageCenterConfig;
import com.imjcker.manager.health.service.MessageCenterService;
import com.imjcker.manager.util.http.HttpClientUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ztzh_tanhh 2019/11/28
 **/
@Slf4j
@Service
public class MessageCenterServiceImpl implements MessageCenterService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MessageCenterConfig messageCenterProperties;
    @Override
    public String sendMessage(String content) throws JsonProcessingException {
        MessageDTO message = new MessageDTO();
        BeanUtils.copyProperties(messageCenterProperties, message);
        message.setReqJnlno(System.currentTimeMillis());
        message.setReqTime(DateTimeFormatter.ofPattern("HHmmss").format(LocalDateTime.now()));
        message.setReqDate(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()));
        message.setMsgContent(content);
        HttpPost httpPost = new HttpPost(message.getUrl());
        String str = objectMapper.writeValueAsString(message);
        HttpEntity httpEntity = new ByteArrayEntity(str.getBytes(StandardCharsets.UTF_8));
        httpPost.setEntity(httpEntity);
        httpPost.setHeader("actionId", "MSC001");
        return HttpClientUtils.post(httpPost);
    }

    @Data
    public static class MessageDTO implements Serializable {
        private static final long serialVersionUID = 496133371882763506L;
        private String reqChannel;
        private String templetNo;
        private String msgTheme;
        private String mobile;
        private String cifNo;
        private String empNo;
        private String toChannel;
        private String url;
        private long reqJnlno;
        private String reqTime;
        private String reqDate;
        private String msgContent;
    }
}
