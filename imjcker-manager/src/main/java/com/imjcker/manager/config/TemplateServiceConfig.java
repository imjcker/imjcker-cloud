package com.imjcker.manager.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author WT
 * @Date 10:19 2019/9/17
 * @Version TemplateServiceConfig v1.0
 * @Desicrption
 */
@Configuration
public class TemplateServiceConfig {


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return new RestTemplate(factory);
    }

}
