package com.imjcker.api.common.http.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.model.SourceLogInfo;
import com.imjcker.api.common.util.SpringUtils;
import com.imjcker.api.common.vo.CommonResult;
import com.imjcker.api.common.vo.Result;
import com.imjcker.api.common.vo.ResultStatusEnum;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: OkHttp封装类
 * @Package com.lemon.common.http.proxy
 * @date 2017年9月20日 下午2:43:46
 */
public class OkHttp1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttp1.class);

    public static final MediaType _JSON = MediaType.parse("application/json; charset=utf-8");


    //*** text/html        是html格式的正文
    //*** text/plain       是无格式正文
    //*** text/xml         忽略xml头所指定编码格式而默认采用us-ascii编码
    //*** application/xml  会根据xml头指定的编码格式来编码：
    public static final MediaType XML = MediaType.parse("text/plain; charset=utf-8");

    /**
     * 同步get请求
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static Result get(String url, Map<String, String> data, Map<String, String> header, Long timeOut,
                             String type, String retryFlag, int retryCount, boolean isIgnoreVerify) {
        url = getRequestUrl(url, data);
        Request.Builder builder = new Request.Builder().url(url);
        //构造请求头
        if (header != null && !header.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = header.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        Request request = builder.build();
        Result result = new Result();
        try {
            long start = System.currentTimeMillis();
            result = execute(request, timeOut, type, retryFlag, retryCount, isIgnoreVerify);
            LOGGER.info("start-execute第三方请求耗时={}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("发生异常:{}",e.getMessage());
            result.setResult(catchException(e));
        }
        return result;
    }

    /**
     * 同步post json
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static Result postJson(String url, Map<String, String> query, String json, Map<String, String> header,
                                  Long timeOut, String type, String retryFlag, int retryCount, boolean isIgnoreVerify) {
        if (query != null && !query.isEmpty()) {
            url = getRequestUrl(url, query);
        }
        RequestBody body = RequestBody.create(_JSON, json);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        //构造请求头
        if (header != null && !header.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = header.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        Request request = builder.build();
        Result result = new Result();
        try {
            long start = System.currentTimeMillis();
            LOGGER.debug("start execute");
            result = execute(request, timeOut, type, retryFlag, retryCount, isIgnoreVerify);
            LOGGER.debug("end execute第三方请求耗时={}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            result.setResult(catchException(e));
        }
        return result;
    }

    /**
     * 同步post xml
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Result postXml(String url, Map<String, String> query, String xml, Map<String, String> header,
                                 Long timeOut, String type, String retryFlag, int retryCount, boolean isIgnoreVerify) {
        if (query != null && !query.isEmpty()) {
            url = getRequestUrl(url, query);
        }
        RequestBody body = RequestBody.create(XML, xml);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        //构造请求头
        if (header != null && !header.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = header.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        Request request = builder.build();
        Result result = new Result();
        try {
            long start = System.currentTimeMillis();
            LOGGER.debug("start-execute");
            result = execute(request, timeOut, type, retryFlag, retryCount, isIgnoreVerify);
            LOGGER.info("第三方请求耗时={}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            result.setResult(catchException(e));
        }
        return result;
    }

    /**
     * 同步post Map
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public static Result postKV(String url, Map<String, String> query, Map<String, String> data, Map<String, String> header,
                                Long timeOut, String type, String retryFlag, int retryCount, boolean isIgnoreVerify) {
        LOGGER.debug("start-postKV");
        if (query != null && !query.isEmpty()) {
            url = getRequestUrl(url, query);
        }
        FormBody.Builder formBuilder = new FormBody.Builder();

        for (Map.Entry<String, String> item : data.entrySet()) {
            formBuilder.add(item.getKey(), item.getValue());
        }
        RequestBody body = formBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(body);
        //构造请求头
        if (header != null && !header.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = header.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntrySet) {
                builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        LOGGER.debug("end-postKV header");
        Request request = builder.build();
        Result  result = new Result();
        try {
            long start = System.currentTimeMillis();
            LOGGER.debug("start-postKV execute");
            result = execute(request, timeOut, type, retryFlag, retryCount, isIgnoreVerify);
            LOGGER.info("第三方请求耗时={}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            result.setResult(catchException(e));
        }
        return result;
    }

    /**
     * 通用同步请求，兼容https
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Result execute(Request request, Long timeout, String type, String retryFlag, int retryCount, boolean isIgnoreVerify) throws IOException {
        Result result = new Result();

        // 1. 构造okHttpClient实例
        OkHttpClient okHttpClient = SpringUtils.getBean(OkHttpClient.class);
        // 重新构造client超时时间
        if (timeout > 30000) {
            okHttpClient = okHttpClient.newBuilder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        }
        // 添加重试拦截器
        if (Boolean.valueOf(retryFlag)) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(new OkHttpRetryInterceptor(retryCount)).build();
        }
        // 2. 发送请求获取结果
        long start = System.currentTimeMillis();
        String strResult = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            int code = response.code();
            if (response.isSuccessful() && response.body() != null) {
               strResult =  response.body().string();
                result.setResult(strResult);
            } else {
                String data = "okHttp响应结果异常:code=" + code + ",message:"+response.message();
                LOGGER.error(data);
                strResult=JSON.toJSONString(new CommonResult(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION, data));
                result.setResult(strResult);
            }
            SourceLogInfo info = SourceLogInfo.builder()
                    .sourceCreateTime(start)
                    .sourceResponseCode(code)
                    .sourceResult(strResult)
                    .sourceTimeout(timeout)
                    .sourceSpendTime(System.currentTimeMillis()-start).build();
            result.setSourceLogInfo(info);
        } catch (Exception e) {
            LOGGER.error("请求出错：{}, 接口超时为：{}， 实际用时：{}", e.getMessage(), timeout, System.currentTimeMillis() - start);
            strResult=JSON.toJSONString(new CommonResult(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION, e.getMessage()));
            result.setResult(strResult);
            SourceLogInfo info = SourceLogInfo.builder()
                    .sourceCreateTime(start)
                    .sourceResponseCode(9999)
                    .sourceResult(strResult)
                    .sourceTimeout(timeout)
                    .sourceSpendTime(System.currentTimeMillis()-start).build();
            result.setSourceLogInfo(info);
        }
        return result;
    }

    /**
     * get方式URL拼接
     *
     * @param url
     * @param map
     * @return
     */
    private static String getRequestUrl(String url, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return url;
        } else {
            StringBuilder newUrl = new StringBuilder(url);
            if (!url.contains("?")) {
                newUrl.append("?");
            }
            int flag = 0;
            for (Map.Entry<String, String> item : map.entrySet()) {
                if (StringUtils.isNotEmpty(item.getKey().trim())) {
                    try {
                        if (flag == 0) {
                            newUrl.append(item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        } else {
                            newUrl.append("&" + item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        }
                        flag++;
                    } catch (Exception e) {
                        LOGGER.error("发生异常" + e);
                    }
                }
            }
            return newUrl.toString();
        }
    }

    public enum ResponseStatus {

        FAIL("请求失败", 500),

        ERROR("请求错误", 500);

        private int resCode;

        private String message;

        private ResponseStatus(String message, int resCode) {
            this.message = message;
            this.resCode = resCode;
        }

        public int getResCode() {
            return resCode;
        }

        public String getMessage() {
            return message;
        }

    }

    public static String catchException(Exception e) {
        LOGGER.error("发生异常" + e);
        String exception = e.toString();
        JSONObject errorInfo = new JSONObject();
        if (exception.indexOf(':') == -1) {
            errorInfo.put("exceptionType", exception);
            errorInfo.put("message", OkHttp1.ResponseStatus.ERROR.getMessage());
            errorInfo.put("inmgrErrorCode", OkHttp1.ResponseStatus.ERROR.getResCode());
            return errorInfo.toJSONString();
//            return com.alibaba.fastjson.JSON.toJSONString(new CommonResult(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION, ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getMessage()));
        }
        //添加异常类型
        errorInfo.put("exceptionType", exception.substring(0, exception.indexOf(':')));
//        //添加异常具体描述
        errorInfo.put("exceptionDesc", exception.substring(exception.indexOf(':') + 1));
        errorInfo.put("message", OkHttp1.ResponseStatus.ERROR.getMessage());
        errorInfo.put("inmgrErrorCode", OkHttp1.ResponseStatus.ERROR.getResCode());
        return errorInfo.toJSONString();
//        return com.alibaba.fastjson.JSON.toJSONString(new CommonResult(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION, ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getMessage()));
    }


    public static class SSLSocketClient {

        //获取这个SSLSocketFactory
        private static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //获取TrustManager
        static TrustManager[] getTrustManager() {
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
        static HostnameVerifier getHostnameVerifier() {
            return (s, sslSession) -> true;
        }
    }
}
