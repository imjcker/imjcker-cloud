package com.imjcker.api.handler;

import com.imjcker.api.common.util.SpringUtils;
import com.imjcker.api.handler.plugin.queue.ApiAsynchronousRequestQueue;
import com.imjcker.api.handler.plugin.queue.ExecutorServiceProperties;
import com.imjcker.api.handler.service.AmqpSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

/**
 * 发送Http请求到第三方
 */
@EnableWebMvc
@EnableCircuitBreaker
@EnableEurekaClient
@EnableFeignClients
@EnableConfigurationProperties({ExecutorServiceProperties.class})
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class HttpClientApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static void main(String[] args) {
        SpringApplication.run(HttpClientApplication.class, args);
    }

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
    @Bean
    public Queue apiEsQueue() {
        return new Queue(AmqpSenderService.DEFAULT_API_ES_TOPIC);
    }

    @Override
    public void run(String... strings) throws Exception {
        List<String> args = Arrays.asList(strings);
        log.debug("启动参数:");
        args.forEach(log::info);
        ApiAsynchronousRequestQueue.getInstance().start();
        log.debug("接口异步请求队列启动完成.");
    }
}
