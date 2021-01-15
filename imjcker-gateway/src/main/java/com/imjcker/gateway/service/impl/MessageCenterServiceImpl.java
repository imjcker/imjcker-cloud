package com.imjcker.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.gateway.config.MessageCenterConfig;
import com.imjcker.gateway.model.MessageDTO;
import com.imjcker.gateway.service.MessageCenterService;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MessageCenterServiceImpl implements MessageCenterService{

    private static Logger log = LoggerFactory.getLogger(MessageCenterServiceImpl.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MessageCenterConfig messageCenterConfig;


    @Override
    public String sendMessage(JSONObject data,boolean sendToPhone){
        String content = this.getContent(data);
        String channel = messageCenterConfig.getToChannel();
        if (sendToPhone){
            channel = channel + "|" + messageCenterConfig.getPhoneChannel();
        }
        MessageDTO message = new MessageDTO();
        message.setUrl(messageCenterConfig.getUrl());
        message.setMobile(messageCenterConfig.getMobile());
        message.setReqChannel(messageCenterConfig.getReqChannel());
        message.setTempletNo(messageCenterConfig.getTempletNo());
        message.setMsgTheme(messageCenterConfig.getMsgTheme());
        message.setEmpNo(messageCenterConfig.getEmpNo());
        message.setToChannel(channel);
        message.setReqJnlno(System.currentTimeMillis());
        message.setReqTime(this.getDateByPattern("HHmmss"));
        message.setReqDate(this.getDateByPattern("yyyyMMdd"));
        message.setMsgContent(content);
        HttpPost httpPost = new HttpPost(message.getUrl());
        String str = "";
        try {
            str = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("json processing error:{}",e);
        }
//        HttpEntity httpEntity = new ByteArrayEntity(str.getBytes());
        HttpEntity httpEntity = null;
        try {
            httpEntity = new ByteArrayEntity(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        httpPost.setEntity(httpEntity);
        httpPost.setHeader("actionId", "MSC001");
        return this.doPost(httpPost);
    }

    private String getDateByPattern(String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    private String doPost(HttpPost httpPost) {

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(50 * 1000)
                .setConnectTimeout(50 * 1000)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String charset = "utf-8";
        String result = "";
        try {
            httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, charset);
            return result;
        } catch (IOException ex) {
            log.error("post error:{}",ex);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ex) {
                    log.error("关闭HttpResponse失败.", ex);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    log.error("关闭HttpClient失败.", ex);
                }
            }
        }
        return result;
    }

    private String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private String getContent(JSONObject data){
        StringBuilder sb = new StringBuilder();
        sb.append(data.getString("appKey"))
                .append(" ")
                .append(data.getString("apiName"))
                .append("|")
                .append("1|")
                .append("接口限流监控|")
                .append(data.getString("content"))
                .append(" 当前策略:")
                .append(data.getString("strategyName"))
//                .append("|1|")
                .append("|")
                .append(data.getString("times")).append("|")
                .append(this.getToday())
                .append("|")
                .append("接口平台限流告警")
                .append("|")
                .append(data.getString("content"))
                .append("|");
        return sb.toString();
    }

    /**
     * 获取微信告警间隔时间
     * @param timeFlag
     * @return
     */
    @Override
    public int getWxIntervalTime(int timeFlag) {
        int result = 0;
        int unit=0;
        switch (timeFlag) {
            case 1:
                unit = messageCenterConfig.getWxMinute();
                result = unit*60;
                break;//分钟
            case 2:
                unit = messageCenterConfig.getWxHour();
                result = unit*60*60;
                break;//小时
            case 3:
                unit = messageCenterConfig.getWxDay();
                result = unit*24*60*60;
                break;//天
            default:
                break;
        }
        return result;
    }

    /**
     * 获取短信告警间隔时间
     * @param timeFlag
     * @return
     */
    @Override
    public int getPhoneIntervalTime(int timeFlag) {
        int result = 0;
        int unit=0;
        switch (timeFlag) {
            case 1:
                unit = messageCenterConfig.getPhoneMinute();
                result = unit*60;
                break;//分钟
            case 2:
                unit = messageCenterConfig.getPhoneHour();
                result = unit*60*60;
                break;//小时
            case 3:
                unit = messageCenterConfig.getPhoneDay();
                result = unit*24*60*60;
                break;//天
            default:
                break;
        }
        return result;
    }
}
