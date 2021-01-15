package com.imjcker.api.handler.config;

import com.imjcker.api.handler.service.AsyncMQService;
import com.imjcker.api.handler.service.KafkaService;
import com.imjcker.api.handler.service.RabbitMQService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thh  2019/7/24
 * @version 1.0.0
 **/
@Configuration
public class ServiceAutoConfigure {
    @Bean
    public AsyncMQService asyncMQService() {
        return new AsyncMQService(kafkaService(), rabbitMQService());
    }

    @Bean
    public KafkaService kafkaService() {
        return new KafkaService();
    }

    @Bean
    public RabbitMQService rabbitMQService() {
        return new RabbitMQService();
    }
}
