package com.imjcker.gateway;

import com.imjcker.gateway.filter.*;
import com.imjcker.gateway.queue.ApiAsynchronousRequestQueue;
import com.imjcker.gateway.queue.ExecutorServiceProperties;
import com.imjcker.gateway.service.AmqpSenderService;
import feign.Retryer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author thh
 */
@EnableConfigurationProperties({ExecutorServiceProperties.class})
@EnableZuulProxy
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan({"com.imjcker.gateway.mapper"})
public class GatewayApplication implements CommandLineRunner {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    @Bean
    public Queue apiEsQueue() {
        return new Queue(AmqpSenderService.DEFAULT_API_ES_TOPIC);
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * pre  0
     */
    @Bean
    public UuidFilter uuidFilter() {
        return new UuidFilter();
    }

    /**
     * 1
     */
    @Bean
    public IpWhitelistFilter ipWhitelistFilter() {
        return new IpWhitelistFilter();
    }

    /**
     * 2
     */
    @Bean
    public UrlFilter urlFilter() {
        return new UrlFilter();
    }

    /**
     * 3
     */
    @Bean
    public AuthFilter accessFilter() {
        return new AuthFilter();
    }

    /**
     * 4
     */
    @Bean
    public RateLimitFilter rateLimiterFilter() {
        return new RateLimitFilter();
    }

    /**
     * 5
     */
    @Bean
    public ValidateFilter validateFilter() {
        return new ValidateFilter();
    }

    /**
     * post 5
     */
    @Bean
    public ResultFilter resultFilter() {
        return new ResultFilter();
    }

    /**
     * 发生error的时候
     */
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @Override
    public void run(String... strings) throws Exception {
        ApiAsynchronousRequestQueue.getInstance().start();
    }
}
