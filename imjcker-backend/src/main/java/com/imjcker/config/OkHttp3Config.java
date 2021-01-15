package com.imjcker.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class OkHttp3Config {
    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("init SSLSocketFactory error: {}", e.getMessage());
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
        return new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .hostnameVerifier(hostnameVerifier())
                .retryOnConnectionFailure(true)
                .connectionPool(connectionPool())
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .dispatcher(dispatcher())
                .build();
    }

}
