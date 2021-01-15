package com.imjcker.api.handler.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmqpSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String DEFAULT_CHARGE_QUEUE = "LemonChargeRuleNew"; // 计费系统默认routingKey

    private static final String DEFAULT_API_MONGODB_TOPIC = "rabbit-topic-api-mongodb"; //

    public void convertAndSend(String queue, Object message) {
        rabbitTemplate.convertAndSend(queue, message);
    }

    public void convertAndSend(Object message) {
        rabbitTemplate.convertAndSend(DEFAULT_API_MONGODB_TOPIC, message);
    }

    public void chargeSend(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }

    public void chargeSend(Object message) {
        chargeSend(DEFAULT_CHARGE_QUEUE, message);
    }





    public static final String DEFAULT_API_ES_TOPIC = "rabbit-topic-api-es-count-source"; //

    public void convertAndSendES(Object message) {
        rabbitTemplate.convertAndSend(DEFAULT_API_ES_TOPIC, message);
    }

}
