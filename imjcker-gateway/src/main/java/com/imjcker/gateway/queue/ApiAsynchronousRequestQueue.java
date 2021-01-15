package com.imjcker.gateway.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author thh  2019/7/10
 * @version 1.0.0
 * 接口异步调用消息队列.由于配置原因,没有实现单例模式,请使用getInstance方法保证唯一性.
 **/
@Component
public class ApiAsynchronousRequestQueue extends LinkedBlockingDeque<Object> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ExecutorServiceProperties executorServiceProperties;
    private static ApiAsynchronousRequestQueue queue;
    private static ExecutorService executorService;
    private boolean flag = false;

    public ApiAsynchronousRequestQueue() {
    }

    public ApiAsynchronousRequestQueue(int capacity) {
        super(capacity);
    }

    @PostConstruct
    public void init() {
        queue = new ApiAsynchronousRequestQueue(executorServiceProperties.getQueueCapacity());
        executorService = Executors.newFixedThreadPool(executorServiceProperties.getThreadPoolSize());
    }

    public static ApiAsynchronousRequestQueue getInstance() {
        return queue;
    }

    public void start() {
        if (!flag) {
            this.flag = true;
        } else {
            throw new IllegalArgumentException("ApiAsynchronousRequestQueue is already running.");
        }
        Thread thread = new Thread(() -> {
            while (flag) {
                try {
                    Object object = take();
                    if (object instanceof ApiHandler) {
                        ApiHandler apiHandler = (ApiHandler) object;
                        executorService.execute(new ApiAsynchronousRequestHandler(apiHandler));
                    }
                } catch (InterruptedException e) {
                    log.error("ApiAsynchronousRequestQueue error. ", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
