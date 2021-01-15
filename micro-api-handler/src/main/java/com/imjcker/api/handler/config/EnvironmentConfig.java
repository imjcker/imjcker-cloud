package com.imjcker.api.handler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author WT
 * @Date 16:44 2019/9/16
 * @Version EnvironmentConfig v1.0
 * @Desicrption
 */
@ConfigurationProperties(prefix = "gatewayClientService")
@Component
public class EnvironmentConfig {

    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
