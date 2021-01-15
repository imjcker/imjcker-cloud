package com.imjcker.manager.config;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 2019-11-12 thh
 * okHttp 3 单例配置
 */
@Configuration
public class OkHttpConfig {
    private static final Logger log = LoggerFactory.getLogger(OkHttpConfig.class);

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
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
        };
    }

    @Bean
    public SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("发生异常 : {}", e.getMessage());
        }
        return null;
    }

    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(500, 2L, TimeUnit.MINUTES);
    }

    @Bean
    public Dispatcher dispatcher() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(128);
        dispatcher.setMaxRequestsPerHost(10);
        return dispatcher;
    }

    @Bean
    public HostnameVerifier hostnameVerifier() {
        return (s, sslSession) -> true;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(getSSLSocketFactory(), x509TrustManager())
                .hostnameVerifier(hostnameVerifier())
                .retryOnConnectionFailure(true)
                .connectionPool(connectionPool())
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .dispatcher(dispatcher())
                .build();
    }
}
