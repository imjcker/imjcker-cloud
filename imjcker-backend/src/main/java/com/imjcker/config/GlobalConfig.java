package com.imjcker.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        OauthConfigProperties.class,
        SiteConfig.class,
        OauthConfigProperties.class
})
public class GlobalConfig {
}

