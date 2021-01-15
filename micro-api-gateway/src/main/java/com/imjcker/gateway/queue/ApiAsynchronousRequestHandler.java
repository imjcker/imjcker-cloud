package com.imjcker.gateway.queue;

/**
 * @author thh  2019/7/10
 * @version 1.0.0
 **/
public class ApiAsynchronousRequestHandler implements Runnable {
    private ApiHandler apiHandler;

    public ApiAsynchronousRequestHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Override
    public void run() {
        if (apiHandler != null) {
            apiHandler.handle();
        }
    }
}
