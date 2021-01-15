package com.lemon.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 
 * @Title EurekaServer
 * @Description 微服务注册与发现服务器启动主类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.zl
 * @version 1.0
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class, args);
    }
}
