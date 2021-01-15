package com.imjcker.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author WT
 * @Date 15:17 2020/4/14
 * @Version KafkaService v1.0
 * @Desicrption
 */
@Component
public class KafkaService {

    private static final String CUSTOMER_AUTH_FLINK_TOPIC = "inmgr-flink-customer-auth";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCustomerFlink(Object message) {
        kafkaTemplate.send(CUSTOMER_AUTH_FLINK_TOPIC, message);
    }


    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
