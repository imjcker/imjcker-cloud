package com.imjcker.spring.cloud.service.mail.queue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class QueueHandler {
    public abstract void handle();

    public final void put() {
        try {
            Queue.getInstance().put(this);
        } catch (Exception e) {
            log.error("Put queue error...", e);
        }
    }
}
