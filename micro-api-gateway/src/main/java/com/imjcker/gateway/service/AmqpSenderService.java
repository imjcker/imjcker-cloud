package com.imjcker.gateway.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmqpSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String DEFAULT_API_ES_TOPIC = "rabbit-topic-api-es-count"; //

    public void convertAndSend(String queue, Object message) {
        rabbitTemplate.convertAndSend(queue, message);
    }

    public void convertAndSend(Object message) {
        rabbitTemplate.convertAndSend(DEFAULT_API_ES_TOPIC, message);
    }

}
