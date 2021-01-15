package com.imjcker.api.manager.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtils {
    public static final MediaType _JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType _XML = MediaType.parse("text/plain; charset=utf-8");

    public static String get(String url, Map<String, String> params, Map<String, String> headers, Long timeout, String retryFlag, int retryCount, boolean isVerify) {
        url = getRequestUrl(url, params);
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();

        String result = null;
        try {
            long start = System.currentTimeMillis();
            result = exe(request, timeout, retryFlag, retryCount, isVerify);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage());
            result = ex(e);
        }
        return result;
    }


    private static String getRequestUrl(String url, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return url;
        } else {
            StringBuilder newUrl = new StringBuilder(url);
            if (!url.contains("?")) {
                newUrl.append("?");
            }
            int flag = 0;
            for (Map.Entry<String, String> item : params.entrySet()) {
                if (!StringUtils.isEmpty(item.getKey().trim())) {
                    try {
                        if (flag == 0) {
                            newUrl.append(item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        } else {
                            newUrl.append("&" + item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                        }
                        flag++;
                    } catch (Exception e) {
                        log.error("build get url error: " + e);
                    }
                }
            }
            return newUrl.toString();
        }
    }

    public static String exe(Request request, Long timeout, String retryFlag, int retryCount, boolean isVerify) {
        OkHttpClient okHttpClient = SpringUtils.getBean(OkHttpClient.class);
        if (timeout > 30000) {
            okHttpClient = okHttpClient.newBuilder().readTimeout((timeout), TimeUnit.MILLISECONDS).build();
            log.info("set timeout: " + timeout);
        }
        if (Boolean.parseBoolean(retryFlag)) {
            okHttpClient.newBuilder().addInterceptor(new OkHttpRetryInterceptor(retryCount)).build();
            log.info(" set retry interceptor: " + retryCount);
        }
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                String data = "code = " + response.code() + ", message: " + response.message();
                log.error(data);
                return JSON.toJSONString(new CommonResult(response.code(), "error", data));
            }
        } catch (Exception e) {
            log.error("request error: " + e.getMessage());
            return JSON.toJSONString(new CommonResult(500, e.getMessage()));
        }
    }

    public static String ex(Exception e) {
        log.error("exception: " + e);
        String ex = e.toString();
        JSONObject errorInfo = new JSONObject();
        if (ex.indexOf(':') == -1) {
            errorInfo.put("exceptionType", ex);
            errorInfo.put("message", "exception");
            errorInfo.put("errorCode", 500);
            return errorInfo.toJSONString();
        }
        errorInfo.put("exceptionType", ex.substring(0, ex.indexOf(':')));
        errorInfo.put("exceptionDesc", ex.substring(ex.indexOf(':') + 1));
        errorInfo.put("message", "exception");
        errorInfo.put("errorCode", 500);
        return errorInfo.toJSONString();
    }
}
