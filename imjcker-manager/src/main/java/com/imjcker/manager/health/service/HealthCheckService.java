package com.imjcker.manager.health.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.manager.health.repository.InstanceRepository;
import com.imjcker.manager.health.model.Instance;
import com.imjcker.manager.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ztzh_tanhh 2019/11/27
 **/
@Slf4j
@Component
public class HealthCheckService implements Job {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MessageCenterService messageCenterService = SpringContextUtils.getBean(MessageCenterService.class);
        InstanceRepository serviceInfoRepository = SpringContextUtils.getBean(InstanceRepository.class);
        List<Instance> instances = serviceInfoRepository.findAll();
        log.info("开始执行服务健康检查任务");
        // todo 后续发送公众号告警
        /*instances.forEach(instance -> {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                String result = HttpClientUtils.get(instance.getHealthCheckUrl());
                Map map = objectMapper.readValue(result, Map.class);
                String status = map.getOrDefault("status", "UNKNOWN").toString();
                if ("404".equals(status)) {
                    String msg = String.format("服务：%s , 节点：%s 未开启健康检查，请重新配置启动。", instance.getAppName(), instance.getInstanceId());
                    log.error(msg);
                    messageCenterService.sendMessage(buildContent(instance.getInstanceId(), instance.getHealthCheckUrl(), msg));
                } else if ("999".equals(status)) {
                    String msg = String.format("服务：%s，节点：%s 拒绝连接", instance.getAppName(), instance.getInstanceId());
                    log.error(msg);
                    messageCenterService.sendMessage(buildContent(instance.getInstanceId(), instance.getHealthCheckUrl(), msg));
                } else if (!"UP".equalsIgnoreCase(status)) {
                    String msg = String.format("服务：%s，节点：%s 状态：%s", instance.getAppName(), instance.getInstanceId(), status);
                    log.error("原始返回内容：{}", result);
                    messageCenterService.sendMessage(buildContent(instance.getInstanceId(), instance.getHealthCheckUrl(), msg));
                }
            } catch (IOException e) {
                log.info("健康检查后台任务异常：{}", e.getMessage());
            } finally {
                HttpClientUtils.release(httpClient, null);
            }

        });*/
    }

    private String buildContent(String serverIp, String serverName, String orginContent) {

        StringBuilder sb = new StringBuilder();
        sb.append(serverName);
        sb.append(" ");
        sb.append(serverIp);
        sb.append("|");
        sb.append("1|");
        sb.append("检测服务健康状态|");
        sb.append(orginContent);
        sb.append("|1|");
        sb.append(formatDate());
        sb.append("|接口平台业务监控-服务不可用告警|");
        sb.append(orginContent);
        sb.append("|");
        return sb.toString();
    }

    private String formatDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}
