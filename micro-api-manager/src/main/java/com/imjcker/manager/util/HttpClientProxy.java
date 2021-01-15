package com.imjcker.manager.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: HttpClientProxy.java
 * <p>Description: HttpClient请求代理
 * <p>Copyright: Copyright © 2017, CQzlll, All Rights Reserved.
 *
 * @author CQzlll.zl
 * @version 1.0
 */
public class HttpClientProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientProxy.class);

    /**
     * @param uri        目标地址
     * @param headers
     * @param jsonObject
     * @return
     */
    public static String postByJson(String uri, Map<String, String> headers, JSONObject jsonObject) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.postByJson(): 调用URI: ");
        loggerMsg.append(uri);
        loggerMsg.append(headers);
        loggerMsg.append(jsonObject);

        HttpPost httpPost = new HttpPost(uri);

        //构造参数数据实体
        if (jsonObject != null) {
            String transJson = jsonObject.toString();
            HttpEntity httpEntity = new StringEntity(transJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(httpEntity);
        }
        //构造请求头
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        String result = null;
        try {
            result = commonInvoke(httpPost);
            loggerMsg.append(result);
            LOGGER.info(loggerMsg.toString());
        } catch (IOException ex) {
            LOGGER.error(loggerMsg.toString(), ex);
        }
        return result;
    }

    public static String postBytes(String uri, Map<String, String> headers, byte[] bytes) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.postBytes(): 调用URI: ");
        loggerMsg.append(uri);
        loggerMsg.append(headers);
        HttpPost httpPost = new HttpPost(uri);
        ByteArrayEntity httpEntity = new ByteArrayEntity(bytes);
        httpPost.setEntity(httpEntity);

        //构造请求头
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        String result = null;

        try {
            result = commonInvoke(httpPost);
            loggerMsg.append(result);
            LOGGER.info(loggerMsg.toString());
        } catch (IOException ex) {
            LOGGER.error(loggerMsg.toString(), ex);
        }
        return result;
    }

    private static String commonInvoke(HttpPost httpPost) throws IOException {
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000 * 1000).setSocketTimeout(3000 * 1000).build();
        httpPost.setConfig(requestConfig);

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;

        try {
            httpClient = HttpClients.createDefault();
            //调用
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpResponse失败.", ex);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }
    }

    public static String get(String uri) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * httpclient get
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) {
        StringBuilder loggerMsg = new StringBuilder();
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            //构造请求头
            if (headers != null && !headers.isEmpty()) {
                Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
                for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                    httpGet.setHeader(headerEntry.getKey(), headerEntry.getValue());
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpResponse失败.", ex);
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }
        LOGGER.info(loggerMsg.toString());
        return resultString;
    }
}
