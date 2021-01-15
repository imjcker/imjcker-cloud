package com.imjcker.manager.scheduled.service.impl;

import ch.ethz.ssh2.StreamGobbler;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.manager.scheduled.service.NetPingService;
import com.lemon.common.util.ExtractUtil;
import com.imjcker.manager.config.MessageCenterConfig;
import com.imjcker.manager.scheduled.dto.MessageDTO;
import com.imjcker.manager.scheduled.mapper.ApiScheduledMapper;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author WT
 * @Date 16:24 2019/7/31
 * @Version ${version}
 */
@Service
public class NetPingServiceImpl implements NetPingService {

    private static final Logger logger = LoggerFactory.getLogger(NetPingServiceImpl.class);

    private static Runtime runtime = Runtime.getRuntime();

    private static Set<String> sourceGroup = new HashSet<>();

    @Autowired
    private ApiScheduledMapper apiScheduledMapper;

    @Autowired
    private MessageCenterConfig messageCenterConfig;

    @Value("${exeCMD.ignoreIp}")
    private String ignoreIP;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * telnet 第三方数据源服务器
     */
    @Override
    public void startPing() {

        // 获取数据源服务地址
        Map<String, String> map = pullSourceAddr();
        logger.info("本次需要telnet 的数据源: {}", map);
        map.forEach(this::eachExecCMD);
        logger.info("检查第三方数据源网络服务完毕,未通网的数据源: {}", sourceGroup);
        if (sourceGroup != null && sourceGroup.size() > 0) {
            // 发送通知
            StringBuilder sb = new StringBuilder();
            sb.append("数据源网络不通");
            sb.append("|");
            sb.append("1");
            sb.append("|");
            sb.append("连接网络失败");
            sb.append("|");
            for (String group : sourceGroup) {
                sb.append(group).append("#");
            }
            sb.append("|");
            sb.append("1");
            sb.append("|");
            sb.append(this.getToday());
            sb.append("|");
            sb.append("数据源网络不通");
            sb.append("|");
            sb.append("第三方网络断路");
            sb.append("|");
            logger.info("通知信息: {}", sb.toString());
            pushMessage(sb.toString());
            sourceGroup.clear();
            logger.info("重置结果集: {}", sourceGroup);
        }

    }

    /**
     * 执行单条命令
     * @param addr
     * @param group
     */
    private void eachExecCMD(String addr,String group) {
        try {
            if (addr.contains("localhost") || addr.contains(ignoreIP)) {
                return;
            }
            String url = null;
            if (addr.startsWith("http")) {
                url = addr;
            }else {
                url = "http://" + addr;
            }
            URI uri = new URI(url);
            String command = "telnet " + uri.getHost() + " " + uri.getPort();
            logger.info("执行命令: {}", command);
            Process process = runtime.exec(command);
            String result = processStdout(process.getInputStream());
            logger.info("执行命令结果: {}", result);
            if (StringUtils.isBlank(result) || !result.contains("Connected")) {
                // 说明 telnet 不通，添加到 set 中
                sourceGroup.add(group+"/" + addr);
            }
        } catch (Exception e) {
            sourceGroup.add(group+"/" + addr);
            logger.error("执行命令: " + addr + " 出错: ", e);
        }
    }

    /**
     * 解析脚本执行返回的结果集
     * @param in
     * @return
     */
    private String processStdout(InputStream in) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
            String line = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                index++;
                if (index >= 2) break;
            }
        }catch (Exception e) {
            logger.error("解析结果错误：", e);
        }
        return sb.toString();
    }

    /**
     * 推送消息
     * @param content
     */
    private void pushMessage(String content) {
        String channel =  messageCenterConfig.getToChannel() + "|" + messageCenterConfig.getPhoneChannel();
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
            logger.error("json processing error:{}",e);
        }
//        HttpEntity httpEntity = new ByteArrayEntity(str.getBytes());
        HttpEntity httpEntity = null;
        try {
            httpEntity = new ByteArrayEntity(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        httpPost.setEntity(httpEntity);
        httpPost.setHeader("actionId", "MSC001");
        String result = this.doPost(httpPost);
        JSONObject object = ExtractUtil.xml2Json(result);
        if ("0000".equals(object.getString("respCode"))){
            logger.info("消息发送成功!!!");
        }
    }

    private String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 转换为　key 为分组，value 为 服务地址的 map
     * @return
     */
    private Map<String, String> pullSourceAddr() {
        Map<String, String> addrMap = new HashMap<>();
        List<Map<String, String>> mapList = apiScheduledMapper.findApiAddr();
        for (Map<String, String> sourceMap : mapList) {
            String group = null;
            String addr = null;
            for (String key : sourceMap.keySet()) {
                if ("groupName".equals(key)) {
                    group = sourceMap.get(key);
                }
                if ("backEndAddress".equals(key)) {
                    addr = sourceMap.get(key);
                }
            }
            // 以后端地址为key,分组为value
            addrMap.put(addr, group);
        }
        return addrMap;
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
        String result = "";
        try {
            httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            logger.info("result: {}", result);
            return result;
        } catch (IOException ex) {
            logger.error("post error:{}",ex);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ex) {
                    logger.error("关闭HttpResponse失败.", ex);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    logger.error("关闭HttpClient失败.", ex);
                }
            }
        }
        return result;
    }
}
