package com.imjcker.gateway.config;

import com.imjcker.gateway.locator.CustomRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class CustomZuulConfig {

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private ServerProperties server;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${asynchronous-service}")
    private String asynchronousService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.zuulProperties, this.asynchronousService, this.discoveryClient);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }
}
