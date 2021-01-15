package com.imjcker.api.handler.plugin.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thh  2019/7/10
 * @version 1.0.0
 * 接口异步回调公共抽象类,在原接口实现类中实现handle方法,并调用putQueue即可.具体实现参考四川国税.
 **/
public abstract class ApiHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //异步/同步标记
    public static final String ASYNCHRONOUS = "asynchronous";
    //回调地址
    public static final String CALLBACK_URL = "callbackUrl";
    // 异步回调topic
    public static final String TOC_API_CALLBACK = "TOC_API_CALLBACK";
    public static final String TOC_RESULT_CALLBACK = "TOC_RESULT_CALLBACK";

    /**
     * 异步回调处理逻辑
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
