package com.imjcker.manager;

import com.imjcker.manager.config.DocumentConfigurationProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月10日 下午5:28:19
 */
@Slf4j
@EnableTransactionManagement
@MapperScan({"com.imjcker.*.*.mapper"})
@EnableCaching
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
@EnableConfigurationProperties({
        DocumentConfigurationProperties.class
})
public class ManagerApplication implements CommandLineRunner {
    private final ApplicationContext applicationContext;

    @Autowired
    public ManagerApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 防Xss攻击过滤器注册
     */
/*    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }*/
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }


    @Override
    public void run(String... strings) {
        log.info("启动参数：");
        for (String arg : strings) {
            log.info(arg);
        }
    }
}
