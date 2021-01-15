package com.imjcker.spring.cloud.service.mail.queue;

public class QueueProcessor implements Runnable {
    private QueueHandler queueHandler;

    public QueueProcessor(QueueHandler queueHandler) {
        this.queueHandler = queueHandler;
    }

    @Override
    public void run() {
        if (queueHandler != null) {
            queueHandler.handle();
        }
    }
}
