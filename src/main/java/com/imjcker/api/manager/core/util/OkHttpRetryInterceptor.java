package com.imjcker.api.manager.core.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class OkHttpRetryInterceptor implements Interceptor {
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
                log.warn("retry count: " + count.get());
            }
            response = chain.proceed(request);
            count.incrementAndGet();
        }
        return response;
    }
}
