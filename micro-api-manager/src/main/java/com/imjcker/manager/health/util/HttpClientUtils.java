package com.imjcker.manager.health.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ztzh_tanhh 2019/11/28
 **/
@Slf4j
public class HttpClientUtils {
    private HttpClientUtils() {
    }

    public static String get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                return result;
            }
        } catch (HttpHostConnectException e) {
            log.error("连接失败：{}, {}", e.getHost(), e.getMessage());
            return "{\"status\":\"999\"}";
        } catch (IOException e) {
            log.error("http client get error");
        } finally {
            release(httpClient, response);
        }
        return "";
    }


    public static String post(HttpPost httpPost) {
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("result: {}", result);
                return result;
            }
        } catch (IOException ex) {
            log.error("{}",ex.getMessage());
        } finally {
            release(httpClient, response);
        }

        return result;
    }


    public static void release(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.error("{}",e.getMessage());
        }
    }
}
