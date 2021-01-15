package com.imjcker.manager.consumer.controller;

import com.imjcker.manager.consumer.service.MongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author ztzh_tanhh 4/23/2020
 * api 调用统计Kafka消息消费服务
 **/
@Slf4j
@Component
public class ApiCountConsumer {
    private final MongoService mongoService;

    public ApiCountConsumer(MongoService mongoService) {
        this.mongoService = mongoService;
    }


    /**
     * 接口调用信息存MongoDB, 统计使用
     *
     * @param message message
     */
    @KafkaListener(topics = "tl-151-inmgr-api-count")
    public void listenCountModel(@Payload String message) {

//        JSONObject jsonObject = JSON.parseObject(message);
//        String uid = jsonObject.get("uid").toString();
//        log.info("Count Model uid : {}", uid);
        mongoService.insertCountModel("inmgrData", "ifaceData", message);
    }
}
