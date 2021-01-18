package com.imjcker.spring.cloud.service.mail.queue;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class Queue extends LinkedBlockingDeque<Object> {
    private static Queue queue;
    private static ExecutorService executorService;
    private boolean flag = false;

    public Queue() {
    }
    public Queue(int capacity){super(capacity);}

    @PostConstruct
    public void init() {
        queue = new Queue(1000);
        executorService = Executors.newFixedThreadPool(32);
    }

    public static Queue getInstance() {
        return queue;
    }

    public void start() {
        if (!flag) {
            this.flag = true;
        } else {
            throw new IllegalArgumentException("Queue is already running");
        }
        Thread thread = new Thread(() -> {
            while (flag) {
                try {
                    Object o = take();
                    if (o instanceof QueueHandler) {
                        QueueHandler handler = (QueueHandler) o;
                        executorService.execute(new QueueProcessor(handler));
                    }

                } catch (InterruptedException e) {
                    log.error("Queue error.", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
