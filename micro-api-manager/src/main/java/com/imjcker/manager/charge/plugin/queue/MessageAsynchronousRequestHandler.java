package com.imjcker.manager.charge.plugin.queue;

/**
 * @author thh  2019/7/10
 * @version 1.0.0
 **/
public class MessageAsynchronousRequestHandler implements Runnable {
    private MessageHandler messageHandler;

    public MessageAsynchronousRequestHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    @Override
    public void run() {
        if (messageHandler != null) {
            messageHandler.handle();
        }
    }
}
