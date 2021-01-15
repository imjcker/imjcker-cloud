package com.imjcker.api.common.http.util;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Author WT
 * @Date 10:30 2019/11/13
 * @Version OkHttpNormalClientUtil v1.0
 * @Desicrption
 */
public class OkHttpNormalClientUtil {

    private static volatile OkHttpClient okHttpClient;

    private OkHttpNormalClientUtil() {

    }

    public static OkHttpClient getInstance() {
        if (null != okHttpClient)
            return okHttpClient;
        synchronized (OkHttpNormalClientUtil.class) {
            if (null != okHttpClient)
                return okHttpClient;
            ConnectionPool pool = new ConnectionPool(500, 2L, TimeUnit.MINUTES);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            okHttpClient = builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                                .readTimeout(30000, TimeUnit.MILLISECONDS)
                                .writeTimeout(30000, TimeUnit.MILLISECONDS)
                                .connectionPool(pool)
                                .sslSocketFactory(getSSLSocketFactory(), (X509TrustManager) getTrustManager()[0])
                                .hostnameVerifier(getHostnameVerifier())
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

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    public static TrustManager[] getTrustManager() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }
        };
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }
}
