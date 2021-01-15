package com.imjcker.api.common.http.proxy;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OkHttp 重试拦截器
 */
public class OkHttpRetryInterceptor implements Interceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private int retryCount;

    public OkHttpRetryInterceptor(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        AtomicInteger count = new AtomicInteger(0);
        while (!response.isSuccessful() && count.get() <= retryCount) {
            if (count.get() > 0) {
                log.warn("第{}次重试", count.get());
            }
            response = chain.proceed(request);
            count.incrementAndGet();
        }
        return response;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

}
