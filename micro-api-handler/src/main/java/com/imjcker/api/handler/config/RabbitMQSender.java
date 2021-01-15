package com.imjcker.api.handler.config;

import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.AsyncModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQSender.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(AsyncModel asyncModel) {
    	String mq = asyncModel.toJsonString();
        rabbitTemplate.convertAndSend("LemonChargeRuleNew", mq);
        LOGGER.info("发送计费RabbitMQ成功");
    }

}
