package com.imjcker.manager.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ztzh_tanhh 2019/11/27
 **/
@Data
@ConfigurationProperties("health-check")
public class HealthCheckProperties {
    private String name = "healthCheck";
    private String cron = "0 0/10 * * * ?";// 每10分钟执行一次
}
