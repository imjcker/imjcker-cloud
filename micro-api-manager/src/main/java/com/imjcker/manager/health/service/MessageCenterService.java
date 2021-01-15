package com.imjcker.manager.health.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author ztzh_tanhh 2019/11/28
 **/
public interface MessageCenterService {
    String sendMessage(String content) throws JsonProcessingException;
}
