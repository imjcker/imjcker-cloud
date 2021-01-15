package com.imjcker.api.handler.plugin.charge;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue LemonChargeRuleNewQueue() {
        return new Queue("LemonChargeRuleNew");
    }

    @Bean
    public Queue apiMongodbQueue() {
        return new Queue("rabbit-topic-api-mongodb");
    }

}
