package com.imjcker.api.handler.config;
/**
 *  gatewayClient环境配置类
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gatewayClientService")
@Component
public class GatewayClientServiceConfig {

    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
