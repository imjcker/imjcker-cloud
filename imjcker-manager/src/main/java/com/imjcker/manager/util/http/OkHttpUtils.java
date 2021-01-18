package com.imjcker.common.http;

import com.imjcker.common.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtils {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType xml = MediaType.parse("text/plain; charset=utf-8");

    public static String get(String url, Map<String, String> headers, Map<String, String> data) {
        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        data.forEach(urlBuilder::addQueryParameter);
        HttpUrl httpUrl = urlBuilder.build();
        Request request = builder.url(httpUrl).build();
        String result = exec(request, 30000L, true, 3);
        log.info("result from third api: {}", request);
        return result;
    }

    public static String post(String url, Map<String, String> headers, String json) {
        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        String result = exec(request, 30000L, true, 3);
        log.info("result from third api: {}", request);
        return result;
    }

    public static String exec(Request request, Long timeout, boolean retry, int retryCount) {
        OkHttpClient okHttpClient = SpringContextUtils.getBean(OkHttpClient.class);

        if (timeout > 30000) {
            okHttpClient = okHttpClient.newBuilder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        }
        if (retry) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(new OkHttpRetryInterceptor(retryCount)).build();
        }

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                log.error("response code: {}, response message: {}", response.code(), response.message());
            }
        } catch (Exception e) {
            log.error("request error");
            return "";
        }


        return null;
    }

    public static class OkHttpRetryInterceptor implements Interceptor {
        private int retryCount;

        public OkHttpRetryInterceptor(int retryCount) {
            this.retryCount = retryCount;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            int count = 0;
            while (count < retryCount) {
                Response response = chain.proceed(request);
                if (response.isSuccessful() && response.body() != null) {
                    return response;
                } else {
                    log.error("response code: {}, response message: {}", response.code(), response.message());
                    log.info("retry: {}", count++);
                }
            }
            return new Response.Builder().build();
        }
    }

}
