package com.imjcker.api.handler.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 特殊处理房管局接口，{}被"符号包裹调不通.  "data":"{"
 * 错误：{"password":"ceshi123456","loginid":"BOCDAZY","regioncode":"4534534543","data":"{"condition":{"OWNER":"白国雪","OWNERID":"510130197310031896",\"BIZFILENO\":"权4464525\","DATASOURCE\":"510100"}}", "signature\":"6d21bdd87d38145db31b314d33065462853d4b1e", "authenticationKey\":\"Qk9DREFaWTpjZXNoaTEyMzQ1NjoyMDE3MTEyMzE2MjMxMA==\",\"nonce\":\"-762181210\" }";
 * 正确：{"password":"ceshi123456","loginid":"BOCDAZY","regioncode":"4534534543","data":{"condition":{"OWNER":"白国雪","OWNERID":"510130197310031896","BIZFILENO":"权4464525","DATASOURCE":"510100"}}, "signature\":"6d21bdd87d38145db31b314d33065462853d4b1e", "authenticationKey\":\"Qk9DREFaWTpjZXNoaTEyMzQ1NjoyMDE3MTEyMzE2MjMxMA==\",\"nonce\":\"-762181210\" }";
 */
public class HouseManagerUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(HouseManagerUtil.class);
    private HouseManagerUtil() {

    }

    public static String houseManagerPost(String url, String requestBody) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(new BasicCookieStore())
                .build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(350 * 1000)
                .setConnectTimeout(350 * 1000)
                .build();
        httpPost.setConfig(requestConfig);
        HttpEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        String result;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            result =  EntityUtils.toString(response.getEntity());
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
        LOGGER.debug("房管局返回结果: {}", result);
        return result;
    }
}
