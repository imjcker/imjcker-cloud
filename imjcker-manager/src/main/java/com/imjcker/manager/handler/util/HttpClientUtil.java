package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.http.proxy.OkHttp;
import com.imjcker.api.common.model.SourceLogInfo;
import com.imjcker.api.common.vo.Result;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * httpClient工具类
 */
public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * @param url
     * @param header
     * @param params
     * @param encoding
     * @param timeout
     * @return
     */
    public static Result doPost(String url, Map<String, String> header, String params, String encoding, long timeout, boolean isIgnoreVerify) throws IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
        if (isIgnoreVerify) {
            httpClient = getCloseableHttpClient(requestConfig);
        } else {
            httpClient = httpClientBuilder.build();
            httpPost.setConfig(requestConfig);
        }
        if (null != header && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        byte[] paramsBytes = params.getBytes(encoding);
        InputStream is = new ByteArrayInputStream(paramsBytes, 0, paramsBytes.length);
        HttpEntity httpEntity = new InputStreamEntity(is, paramsBytes.length);
        httpPost.setEntity(httpEntity);
        httpPost.releaseConnection();
        long httpClientEndTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        LOGGER.info("responseTime耗时：{} ms", System.currentTimeMillis() - httpClientEndTime);
        Result result = new Result();
        try {
            result = getResponse(response, encoding, httpClientEndTime, timeout);
        } catch (Exception e) {
            result = catchExceptipn(e, httpClientEndTime);
        } finally {
            // 释放资源
            try {
                is.close();
            } catch (Exception e) {
                LOGGER.error("inputStream关闭异常");
            }
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.error("response关闭异常");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
                LOGGER.error("httpClient关闭异常");
            }
        }
        return result;
    }

    /**
     * @param url
     * @param header
     * @param params
     * @param encoding
     * @param timeout
     * @return
     */
    public static Result doGet(String url, Map<String, String> header, String params, String encoding, long timeout) throws IOException {
        CloseableHttpClient httpClient;
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpGet httpGet = new HttpGet(new StringBuilder(url).append("?").append(params).toString());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
        httpGet.setConfig(requestConfig);
        httpClient = httpClientBuilder.build();
        if (null != header) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        Result result = new Result();
        long queryTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            LOGGER.error("responseTime耗时：{} ms", System.currentTimeMillis() - queryTime);
            result = getResponse(response, encoding, queryTime, timeout);
        } catch (Exception e) {
            result = catchExceptipn(e, queryTime);
        } finally {
            // 释放资源
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.error("response关闭异常");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
                LOGGER.error("httpClient关闭异常");
            }
        }
        return result;
    }

    /**
     * @param url
     * @param header
     * @param params
     * @param encoding
     * @param timeout
     * @return
     */
    public static Result jsonPost(String url, Map<String, String> header, JSONObject params, String encoding, long timeout) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
        httpPost.setConfig(requestConfig);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        StringEntity entity = new StringEntity(params.toString());
        entity.setContentEncoding(encoding);
        entity.setContentType("application/json");//发送json数据需要设置contentType
        httpPost.setEntity(entity);
        long queryTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Result result = new Result();
        try {
            result = getResponse(response, encoding, queryTime, timeout);
        } catch (Exception e) {
            result = catchExceptipn(e, queryTime);
        } finally {
            // 释放资源
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.error("response关闭异常");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
                LOGGER.error("httpClient关闭异常");
            }
        }
        return result;
    }

    /**
     * @param url
     * @param header
     * @param params
     * @param encoding
     * @param timeout
     * @return
     */
    public static Result mapPost(String url, Map<String, String> header, Map<String, String> params, String encoding, long timeout, boolean isIgnoreVerify) throws IOException {
        CloseableHttpClient httpClient;
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpPost httpPost = new HttpPost(url);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
//        httpPost.setConfig(requestConfig);
        if (null != header && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
        if (isIgnoreVerify) {
            httpClient = getCloseableHttpClient(requestConfig);
        } else {
            httpClient = httpClientBuilder.build();
            httpPost.setConfig(requestConfig);
        }
        //设置参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
        }
        if (list.size() > 0) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encoding);
            httpPost.setEntity(entity);
        }
        long queryTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Result result = new Result();
        try {
            result = getResponse(response, encoding, queryTime, timeout);
        } catch (Exception e) {
            result = catchExceptipn(e, queryTime);
        } finally {
            // 释放资源
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.error("response关闭异常");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
                LOGGER.error("httpClient关闭异常");
            }
        }
        return result;
    }

    /**
     * @param url
     * @param header
     * @param params
     * @param encoding
     * @param timeout
     * @return
     */
    public static Result mapGet(String url, Map<String, Object> header, Map<String, Object> params, String encoding, long timeout) throws IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> elem = (Map.Entry<String, Object>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
        }
        String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(list, encoding));
        StringBuilder builder = new StringBuilder(url).append("?").append(paramStr);
//        HttpGet httpGet = new HttpGet(url);
        HttpGet httpGet = new HttpGet(builder.toString());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) timeout).setSocketTimeout((int) timeout).build();
        httpGet.setConfig(requestConfig);
        for (Map.Entry<String, Object> entry : header.entrySet()) {
            httpGet.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
        }
        long queryTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        Result result = new Result();
        try {
            result = getResponse(response, encoding, queryTime, timeout);
        } catch (Exception e) {
            result = catchExceptipn(e, queryTime);
        } finally {
            // 释放资源
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.error("response关闭异常");
            }
            try {
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
                LOGGER.error("httpClient关闭异常");
            }
        }
        return result;
    }


    /**
     * 获取返回结果
     *
     * @param response
     * @param encoding
     * @return
     * @throws IOException
     */
    private static Result getResponse(CloseableHttpResponse response, String encoding, long queryTime, long timeout) throws IOException {
        LOGGER.debug("status={}", response.getStatusLine().getStatusCode());
        Result result = new Result();
        int status = response.getStatusLine().getStatusCode();
        long time = System.currentTimeMillis();
        if (HttpStatus.SC_OK == status) {
            LOGGER.debug("getResponse耗时：{} ms", System.currentTimeMillis() - time);
            String strResult = EntityUtils.toString(response.getEntity(), encoding);
            result.setResult(strResult);
            //包装es存储数据
            SourceLogInfo info = SourceLogInfo.builder()
                    .sourceCreateTime(queryTime)
                    .sourceResponseCode(status)
                    .sourceResult(strResult)
                    .sourceTimeout(timeout)
                    .sourceSpendTime(System.currentTimeMillis() - queryTime).build();
            result.setSourceLogInfo(info);
            return result;
        } else {
            LOGGER.error("请求失败!");
            //包装es存储数据
            SourceLogInfo info = SourceLogInfo.builder()
                    .sourceCreateTime(queryTime)
                    .sourceResponseCode(status)
                    .sourceResult("请求失败!")
                    .sourceTimeout(timeout)
                    .sourceSpendTime(System.currentTimeMillis() - queryTime).build();
            result.setSourceLogInfo(info);
            result.setResult(null);
            return result;
        }
    }

    /**
     * 忽略https证书验证
     *
     * @return
     */
    private static CloseableHttpClient getCloseableHttpClient(RequestConfig requestConfig) {
        SSLContext sslContext = getSSLContext();
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", new SSLConnectionSocketFactory(sslContext)).build();
        PoolingHttpClientConnectionManager mananger = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        mananger.setMaxTotal(500);//设置最大连接数
        mananger.setDefaultMaxPerRoute(200);//设置每个路由基础连接
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(mananger)
                .build();
        return httpClient;
    }

    private static SSLContext getSSLContext() {
        try {
            // 这里可以填两种值 TLS和LLS , 具体差别可以自行搜索
            SSLContext sc = SSLContext.getInstance("TLS");
            // 构建新对象
            X509TrustManager manager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                // 这里返回Null
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[]{manager}, null);
            return sc;
        } catch (Exception e) {
            LOGGER.error("发生异常 : {}", e.getMessage());
        }
        return null;
    }

    /**
     * 包装异常
     *
     * @param e
     * @return
     */
    private static Result catchExceptipn(Exception e, long start) {
        LOGGER.error("httpClient-请求异常，{}", e.getMessage());
        JSONObject errorInfo = new JSONObject();
        errorInfo.put("inmgrErrorCode", OkHttp.ResponseStatus.FAIL.getResCode());
        errorInfo.put("message", OkHttp.ResponseStatus.FAIL.getMessage());
        errorInfo.put("exceptionDesc", e.getMessage());
        Result result = new Result();
        //包装存到ES数据
        SourceLogInfo info = SourceLogInfo.builder()
                .sourceCreateTime(start)
                .sourceResponseCode(9999)
                .sourceResult(errorInfo.toJSONString())
                .sourceSpendTime(System.currentTimeMillis() - start).build();
        result.setSourceLogInfo(info);
        result.setResult(errorInfo.toJSONString());

        return result;
    }
}
