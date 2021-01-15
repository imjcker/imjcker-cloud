package com.imjcker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "site")
public class SiteConfig {
    private String domain = "imjcker.com";
}
