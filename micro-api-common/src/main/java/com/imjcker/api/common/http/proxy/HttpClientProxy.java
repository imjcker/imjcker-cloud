package com.imjcker.api.common.http.proxy;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
     *
     * @param uri 目标地址
     * @param headers
     * @param params
     * @return
     */
    public static String post(String uri, Map<String, String> headers, Map<String, String> params) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.post(): 调用url: ");
        loggerMsg.append(uri);
        loggerMsg.append("(头信息: ");
        loggerMsg.append(headers);
        loggerMsg.append(" | 参数: ");
        loggerMsg.append(params);
        loggerMsg.append(")");
        HttpPost httpPost = new HttpPost(uri);

        //构造参数数据实体
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        if(params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
            for(Map.Entry<String, String> paramEntry : paramEntrySet) {
                parameters.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
        }
        //构造请求头
        if(headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
            for(Map.Entry<String, String> headerEntry : headerEntrySet) {
                httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        String result = null;

        try {
            result = commonInvoke(httpPost);
            loggerMsg.append("成功(返回数据: ");
            loggerMsg.append(result);
            loggerMsg.append(").");
            LOGGER.debug(loggerMsg.toString());
        } catch (IOException ex) {
            loggerMsg.append("失败.原因: 出现异常.");
            LOGGER.error(loggerMsg.toString(), ex);
        }
        return result;
    }

    /**
    *
    * @param uri 目标地址
    * @param headers
    * @param params
    * @return
    */
   public static Map debugByPost(String uri, Map<String, String> headers, Map<String, String> params) {
       StringBuilder loggerMsg = new StringBuilder();
       loggerMsg.append("HttpClientProxy.post(): 调用url: ");
       loggerMsg.append(uri);
       loggerMsg.append("(头信息: ");
       loggerMsg.append(headers);
       loggerMsg.append(" | 参数: ");
       loggerMsg.append(params);
       loggerMsg.append(")");

       HttpPost httpPost = new HttpPost(uri);

       //设置请求和传输超时时间
       RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(350 * 1000).setSocketTimeout(350 * 1000).build();
       httpPost.setConfig(requestConfig);

       //构造参数数据实体
       List<NameValuePair> parameters = new ArrayList<NameValuePair>();
       if(params != null && !params.isEmpty()) {
           Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
           for(Map.Entry<String, String> paramEntry : paramEntrySet) {
               parameters.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
           }
           UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
           httpPost.setEntity(urlEncodedFormEntity);
       }
       //构造请求头
       if(headers != null && !headers.isEmpty()) {
           Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
           for(Map.Entry<String, String> headerEntry : headerEntrySet) {
               httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
           }
       }
       httpPost.setHeader("Accept","application/json,text/javascript, */*");
       Map resultMap = new HashMap();
       try {
    	   resultMap = debugInvoke(httpPost);
           loggerMsg.append("成功(返回数据: ");
           loggerMsg.append("数据包装为MAP");
           loggerMsg.append(").");
           LOGGER.debug(loggerMsg.toString());
       } catch (IOException ex) {
           loggerMsg.append("失败.原因: 出现异常.");
           LOGGER.error(loggerMsg.toString(), ex);
       }
       return resultMap;
   }

    public static String Get(String url, Map<String, String> headers, Map<String, String>params) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.get(): 调用url: ");
        loggerMsg.append(url);
        loggerMsg.append("(头信息: ");
        loggerMsg.append(headers);
        loggerMsg.append(" | 参数: ");
        loggerMsg.append(params);
        loggerMsg.append(")");

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
            if(headers != null && !headers.isEmpty()) {
                Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
                for(Map.Entry<String, String> headerEntry : headerEntrySet) {
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
            if(httpclient != null) {
                try {
                	httpclient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }

        loggerMsg.append("成功(返回数据: ");
		loggerMsg.append(resultString);
		loggerMsg.append(").");
		LOGGER.debug(loggerMsg.toString());

        return resultString;
    }

    /**
     * 【调试API】调用的get方法
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static Map debugByGet(String url, Map<String, String> headers, Map<String, String>params) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.get(): 调用url: ");
        loggerMsg.append(url);
        loggerMsg.append("(头信息: ");
        loggerMsg.append(headers);
        loggerMsg.append(" | 参数: ");
        loggerMsg.append(params);
        loggerMsg.append(")");

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        StringBuffer sb = new StringBuffer();
        Map resultMap = new HashMap();
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

            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(350 * 1000).setSocketTimeout(350 * 1000).build();
            httpGet.setConfig(requestConfig);

            //构造请求头
            if(headers != null && !headers.isEmpty()) {
                Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
                for(Map.Entry<String, String> headerEntry : headerEntrySet) {
                	httpGet.setHeader(headerEntry.getKey(), headerEntry.getValue());
                }
            }
            httpGet.setHeader("Accept","application/json,text/javascript, */*");
            // 执行请求
            response = httpclient.execute(httpGet);
            //获取所有返回的头部信息
            Header allHeaders[] = response.getAllHeaders();
//            sb.append(response.getStatusLine().getStatusCode()+"\n");
            resultMap.put("statusCode", response.getStatusLine().getStatusCode());
            Integer i = 0;
            while (i < allHeaders.length) {
            	sb.append(StringUtils.join(allHeaders[i].getName(),":",allHeaders[i].getValue())+"\n");
            	i++;
            }
            resultMap.put("responseHeader", sb);
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//            sb.append(resultString);
            resultMap.put("resultString", resultString);
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
            if(httpclient != null) {
                try {
                	httpclient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }

        loggerMsg.append("成功(返回数据: ");
		loggerMsg.append(sb);
		loggerMsg.append(").");
		LOGGER.debug(loggerMsg.toString());

        return resultMap;
    }

    /**
     *
     * @param uri 目标地址
     * @param headers
     * @param params
     * @return
     */
    public static String postByJson(String uri, Map<String, String> headers, Map<String, String> params) {
        StringBuilder loggerMsg = new StringBuilder();
        loggerMsg.append("HttpClientProxy.postByJson(): 调用URI: ");
        loggerMsg.append(uri);
        loggerMsg.append("(头信息: ");
        loggerMsg.append(headers);
        loggerMsg.append(" | 参数: ");
        loggerMsg.append(params);
        loggerMsg.append(")");

        HttpPost httpPost = new HttpPost(uri);

        //构造参数数据实体
        JSONObject jsonObject = new JSONObject();
        if(params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
            for(Map.Entry<String, String> paramEntry : paramEntrySet) {
                jsonObject.put(paramEntry.getKey(), paramEntry.getValue());
            }
            String transJson = jsonObject.toString();
            HttpEntity httpEntity = new StringEntity(transJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(httpEntity);
        }
        //构造请求头
        if(headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
            for(Map.Entry<String, String> headerEntry : headerEntrySet) {
                httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        String result = null;

        try {
            result = commonInvoke(httpPost);
            loggerMsg.append("成功(返回数据: ");
            loggerMsg.append(result);
            loggerMsg.append(").");
            LOGGER.debug(loggerMsg.toString());
        } catch (IOException ex) {
            loggerMsg.append("失败.原因: 出现异常.");
            LOGGER.error(loggerMsg.toString(), ex);
        }
        return result;
    }

    /**
    *
    * @param uri 目标地址
    * @param headers
    * @param jsonObject
    * @return
    */
   public static String postByJson(String uri, Map<String, String> headers, JSONObject jsonObject) {
       StringBuilder loggerMsg = new StringBuilder();
       loggerMsg.append("HttpClientProxy.postByJson(): 调用URI: ");
       loggerMsg.append(uri);
       loggerMsg.append("(头信息: ");
       loggerMsg.append(headers);
       loggerMsg.append(" | 参数: ");
       loggerMsg.append(jsonObject);
       loggerMsg.append(")");

       HttpPost httpPost = new HttpPost(uri);

       //构造参数数据实体
       if(jsonObject != null) {
           String transJson = jsonObject.toString();
           HttpEntity httpEntity = new StringEntity(transJson, ContentType.APPLICATION_JSON);
           httpPost.setEntity(httpEntity);
       }
       //构造请求头
       if(headers != null && !headers.isEmpty()) {
           Set<Map.Entry<String, String>> headerEntrySet = headers.entrySet();
           for(Map.Entry<String, String> headerEntry : headerEntrySet) {
               httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
           }
       }

       String result = null;

       try {
           result = commonInvoke(httpPost);
           loggerMsg.append("成功(返回数据: ");
           loggerMsg.append(result);
           loggerMsg.append(").");
           LOGGER.debug(loggerMsg.toString());
       } catch (IOException ex) {
           loggerMsg.append("失败.原因: 出现异常.");
           LOGGER.error(loggerMsg.toString(), ex);
       }
       return result;
   }

    private static String commonInvoke(HttpPost httpPost) throws IOException {
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(350 * 1000).setConnectTimeout(350 * 1000).build();
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
            if(httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpResponse失败.", ex);
                }
            }
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }
    }

    /**
     * 用于【调试API】的返回
     * @param httpPost
     * @return
     * @throws IOException
     */
    private static Map debugInvoke(HttpPost httpPost) throws IOException {
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(350 * 1000).setConnectTimeout(350 * 1000).build();
        httpPost.setConfig(requestConfig);

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String charset = "utf-8";

        try {
            httpClient = HttpClients.createDefault();

            //调用
            httpResponse = httpClient.execute(httpPost);
            //获取所有返回的头部信息
            Header headers[] = httpResponse.getAllHeaders();
            Map resultMap = new HashMap();
            StringBuffer sb = new StringBuffer();
//            sb.append(httpResponse.getStatusLine().getStatusCode()+"\n");
            resultMap.put("statusCode", httpResponse.getStatusLine().getStatusCode());
            Integer i = 0;
            while (i < headers.length) {
            	sb.append(StringUtils.join(headers[i].getName(),":",headers[i].getValue())+"\n");
            	i++;
            }
            resultMap.put("responseHeader", sb);
//            sb.append("\n");
            HttpEntity httpEntity = httpResponse.getEntity();
//            sb.append(EntityUtils.toString(httpEntity,charset));
            String resultJson = EntityUtils.toString(httpEntity,charset);
            resultMap.put("resultString",resultJson);
            return resultMap;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if(httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpResponse失败.", ex);
                }
            }
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    LOGGER.error("关闭HttpClient失败.", ex);
                }
            }
        }
    }

    private HttpClientProxy() {
        throw new RuntimeException("禁止实例化HttpClient请求代理类.");
    }

}
