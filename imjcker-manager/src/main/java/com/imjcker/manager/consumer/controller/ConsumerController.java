package com.imjcker.manager.consumer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.consumer.service.EsService;
import com.imjcker.manager.consumer.service.MongoService;
import com.imjcker.manager.elastic.model.QueryInfo;
import com.imjcker.manager.elastic.model.SourceLogInfo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class ConsumerController {
    private final MongoService mongoService;
    private final EsService esService;

    @Value("${es.index}")
    private String index;//上游ES索引

    @Value("${es.type}")
    private String type;//上游ES类型

    @Value("${es.indexZuul}")
    private String indexZuul;//下游ES索引

    @Value("${es.typeZuul}")
    private String typeZuul;//下游ES类型

    @Autowired
    public ConsumerController(MongoService mongoService, EsService esService) {
        this.mongoService = mongoService;
        this.esService = esService;

    }

    /**
     * 接口结果存MongoDB
     *
     * @param message result
     * @param channel channel
     */
    @RabbitHandler
    @RabbitListener(queues = "rabbit-topic-api-mongodb")
    public void dataCaptureResult(Message message, Channel channel) {

        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        JSONObject jsonObject = JSON.parseObject(json);

        String dbName = jsonObject.getString("dbName");
        String collectionName = jsonObject.getString("collectionName");
        log.debug("mongo topic dbName : {}, collection : {}", dbName, collectionName);
        mongoService.insert(dbName, collectionName, jsonObject.getString("asyncModel"));
    }

    /**
     * 数据源调用统计,存ES
     *
     * @param message message
     */
    @RabbitHandler
    @RabbitListener(queues = "rabbit-topic-api-es-count-source", containerFactory = "taskContainerFactory")
    public void dataCaptureSourceResult(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        SourceLogInfo info = JSON.parseObject(json, SourceLogInfo.class);
        esService.insert(index, type, info);
        log.debug("上游ES index:{},type:{},uid:{},sourceName:{}", index, type, info.getUid(), info.getSourceName());
    }

    /**
     * 下游请求存入ES中
     *
     * @param message
     * @param
     */
    @RabbitHandler
    @RabbitListener(queues = "rabbit-topic-api-es-count", containerFactory = "taskContainerFactory")
    public void dataCaptureResult(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        QueryInfo info = JSON.parseObject(json, QueryInfo.class);
        esService.insert(indexZuul, typeZuul, info);
        log.debug("下游ES index:{},type:{},uid:{}", indexZuul, typeZuul, info.getUid());
    }
}
