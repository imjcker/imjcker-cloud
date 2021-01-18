package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author WT
 * @Date 10:26 2019/9/17
 * @Version MicroZuulWebService v1.0
 * @Desicrption 使用restTemplate 负载均衡访问 MICRO-ZUUL-WEB-SERVICE
 */
@Service
public class MicroZuulWebService {

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject apiInfo(JSONObject obj) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<JSONObject> entity = new HttpEntity<>(obj, headers);
        return restTemplate.postForObject("http://MICRO-ZUUL-WEB-SERVICE/gateway/gateway/gateway/api/debugInfo",
                entity, JSONObject.class);
    }

    public JSONObject requestApi(JSONObject obj) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<JSONObject> entity = new HttpEntity<>(obj, headers);
        return restTemplate.postForObject("http://MICRO-ZUUL-WEB-SERVICE/gateway/gateway/gateway/api/debugging",
                entity, JSONObject.class);
    }
}
