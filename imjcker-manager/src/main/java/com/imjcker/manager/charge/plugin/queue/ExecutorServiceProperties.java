package com.imjcker.manager.charge.plugin.queue;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author thh  2019/7/16
 * @version 1.0.0
 **/
@ConfigurationProperties(prefix = "executor-service")
public class ExecutorServiceProperties {
    private int threadPoolSize = 64;

    private int queueCapacity = 10000;

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
