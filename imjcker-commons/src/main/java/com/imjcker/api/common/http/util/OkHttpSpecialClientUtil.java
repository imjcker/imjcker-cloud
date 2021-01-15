package com.imjcker.api.common.http.util;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;

import javax.net.ssl.X509TrustManager;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Author WT
 * @Date 10:53 2019/11/13
 * @Version OkHttpSpecialClientUtil v1.0
 * @Desicrption
 */
public class OkHttpSpecialClientUtil {

    private static volatile OkHttpClient okHttpClient;

    private OkHttpSpecialClientUtil() {
        throw new RuntimeException("不允许创建实例");
    }

    public static OkHttpClient getInstance() {
        if (null != okHttpClient)
            return okHttpClient;
        synchronized (OkHttpNormalClientUtil.class) {
            if (null != okHttpClient)
                return okHttpClient;
            ConnectionPool pool = new ConnectionPool(200, 2L, TimeUnit.MINUTES);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            okHttpClient = builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                    .readTimeout(60000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS)
                    .connectionPool(pool)
                    .sslSocketFactory(OkHttpNormalClientUtil.getSSLSocketFactory(), (X509TrustManager) OkHttpNormalClientUtil.getTrustManager()[0])
                    .hostnameVerifier(OkHttpNormalClientUtil.getHostnameVerifier())
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .addInterceptor(chain -> {
                        Request.Builder builder1 = chain
                                .request().newBuilder().addHeader("Connection", "close");
                        return chain.proceed(builder1.build());
                    })
                    .build();
            return okHttpClient;
        }
    }
}
