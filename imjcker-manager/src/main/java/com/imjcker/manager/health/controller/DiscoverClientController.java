package com.imjcker.manager.health.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.manager.health.repository.InstanceRepository;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.health.model.Instance;
import com.imjcker.manager.health.util.HttpClientUtils;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ztzh_tanhh 2019/11/28
 **/
@Slf4j
@RestController
@RequestMapping("/health")
public class DiscoverClientController {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final EurekaClient eurekaClient;
    private final InstanceRepository instanceRepository;

    public DiscoverClientController(EurekaClient eurekaClient, InstanceRepository instanceRepository) {
        this.eurekaClient = eurekaClient;
        this.instanceRepository = instanceRepository;
    }

    /**
     * 获取节点信息
     *
     * @return list
     */
    @PostMapping("/getApps")
    public CommonResult getApps() {
        Applications applications = eurekaClient.getApplications();
        List<Application> applicationList = applications.getRegisteredApplications();
        //所有已经注册的节点
        List<Instance> instanceListAll = new ArrayList<>();
        applicationList.forEach(application -> {
            List<InstanceInfo> instances = application.getInstances();
            instances.forEach(instanceInfo -> {
                Instance instance = new Instance();
                BeanUtils.copyProperties(instanceInfo, instance);
                instance.setStatus("gray");
                instanceListAll.add(instance);
            });
        });
        //健康检查的节点
        List<Instance> instanceListRegistered = instanceRepository.findAll();
        //发送请求，确定状态
//        ConcurrentSkipListSet instances = new ConcurrentSkipListSet<Instance>();
        ArrayList<Instance> instances = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            CompletableFuture[] futures = instanceListRegistered.stream().map(instance ->
                    CompletableFuture.supplyAsync(() -> get(instance.getHealthCheckUrl()), pool)
                            .whenComplete((rst, exp) -> {
                                try {
                                    Map map = objectMapper.readValue(rst, Map.class);
                                    String status = map.getOrDefault("status", "UNKNOWN").toString();
                                    instance.setStatus(status.equalsIgnoreCase("UP") ? "green" : "red");
                                    instances.add(instance);
                                } catch (IOException e) {
                                    log.error("health check info parse error.");
                                }
                            })
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        } catch (Exception e) {
            log.error("解析健康检查结果出错：{}", e.getMessage());
        } finally {
            pool.shutdown();
        }
        //做并集
        instanceListAll.removeAll(instances);
        instanceListAll.addAll(instances);
        Collections.reverse(instanceListAll);
        return CommonResult.success(instanceListAll);
    }


    /**
     * 添加健康检查服务
     *
     * @return list
     */
    @PostMapping("/addHealthCheck")
    public CommonResult addHealthCheck(@RequestBody JSONObject jsonObject) {

        List<Application> applications = eurekaClient.getApplications().getRegisteredApplications();
        applications.forEach(application ->
                application.getInstances().forEach(instanceInfo -> {
                    if (jsonObject.getString("instanceId").equalsIgnoreCase(instanceInfo.getInstanceId())) {
                        Instance instance = new Instance();
                        BeanUtils.copyProperties(instanceInfo, instance);
                        instanceRepository.save(instance);
                    }
                })
        );
        return CommonResult.success();
    }

    /**
     * 删除健康检查服务
     *
     * @return list
     */
    @PostMapping("/delHealthCheck")
    public CommonResult delHealthCheck(@RequestBody JSONObject jsonObject) {
        instanceRepository.delete(jsonObject.getString("instanceId"));
        return CommonResult.success();
    }

    /**
     * health check specified http client get method
     *
     * @param url req url
     * @return check result
     */
    public static String get(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (HttpHostConnectException e) {
            log.error("连接失败：{}, {}", e.getHost(), e.getMessage());
            return "{\"status\":\"999\"}";
        } catch (IOException e) {
            log.error("http client get error");
        } finally {
            HttpClientUtils.release(httpClient, response);
        }
        return "";
    }

}
