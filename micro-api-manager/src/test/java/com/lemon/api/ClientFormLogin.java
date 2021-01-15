package com.lemon.api;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ClientFormLogin {

    public static void main(String[] args) throws IOException {

        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        String url = "http://172.32.12.244:2331/api/login";
        HttpPost httpPost = new HttpPost(url);

        String requestBody = "{\"username\":\"SCtianfutest\",\"password\":\"TF123456*\"}";

        HttpEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();

        System.out.println(response.getStatusLine());
    }
}
