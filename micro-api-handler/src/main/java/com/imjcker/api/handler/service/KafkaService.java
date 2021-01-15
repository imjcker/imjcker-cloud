package com.imjcker.api.handler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

    private static final String API_COUNT_TOPIC = "tl-151-inmgr-api-count";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCountMessage(Object message) {
        kafkaTemplate.send(API_COUNT_TOPIC, message);
    }

    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

}
