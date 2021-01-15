package com.imjcker.gateway.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接口异步公共抽象类,在原接口实现类中实现handle方法,并调用putQueue即可.具体实现参考四川国税.
 **/
public abstract class ApiHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 异步处理逻辑
     */
    public abstract void handle();
    /**
     * 推送到本地队列: ApiAsynchronousRequestQueue
     */
    public final void putQueue(){
        try {
            ApiAsynchronousRequestQueue.getInstance().put(this);
        } catch (Exception e) {
            log.error("Add queue error: ", e);
        }
    }
}
