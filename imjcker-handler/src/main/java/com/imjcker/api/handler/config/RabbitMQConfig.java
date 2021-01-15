package com.imjcker.api.handler.config;

import com.imjcker.api.handler.plugin.queue.ApiHandler;
import com.imjcker.api.handler.plugin.queue.ApiHandler;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 配置类
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 异步请求: 推送缓存结果队列
     *
     * @return queue
     */
    @Bean
    public Queue tocResultCallback() {
        return new Queue(ApiHandler.TOC_RESULT_CALLBACK);
    }

    /**
     * 异步请求: 推送请求参数队列
     *
     * @return queue
     */
    @Bean
    public Queue tocApiCallback() {
        return new Queue(ApiHandler.TOC_API_CALLBACK);
    }
}
