package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.InterfaceCountModel;
import com.imjcker.api.handler.model.MongoRequest;
import com.imjcker.api.handler.util.ConstantEnum;
import com.imjcker.api.handler.util.JsonUtil;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.InterfaceCountModel;
import com.imjcker.api.handler.model.MongoRequest;
import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.util.AsyncCollectionUtil;
import com.imjcker.api.handler.util.ConstantEnum;
import com.imjcker.api.handler.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thh  2019/7/24
 * @version 1.0.0
 **/
@Service
public class AsyncMQService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncMQService.class);

    private final KafkaService kafkaService;
    private final RabbitMQService rabbitMQService;

    @Autowired
    public AsyncMQService(KafkaService kafkaService, RabbitMQService rabbitMQService) {
        this.kafkaService = kafkaService;
        this.rabbitMQService = rabbitMQService;
    }

    /**
     * 结果和接口统计处理
     *
     * @param startTime   请求开始时间
     * @param countModel  统计模型
     * @param apiInfo     apiInfo
     * @param thirdResult 最终返回的结果
     * @param async       接口信息收集模型
     */
    public void sendMQ(final long startTime, InterfaceCountModel countModel, final ApiInfoVersions apiInfo, final JSONObject thirdResult, AsyncModel async) {
        try {
            AsyncCollectionUtil.putAsyncModel(async);
            AsyncCollectionUtil.putCharge(apiInfo.getCharge());

            AsyncCollectionUtil.putAsyncTrueResult(thirdResult);

            countModel.setResponseTime(System.currentTimeMillis() - startTime);
            countModel.setApiName(apiInfo.getApiName());
            AsyncModel asyncModel = AsyncCollectionUtil.getAsyncModel();
            countModel.setResultStatus(asyncModel.getSourceStatus());
            countModel.setCached(asyncModel.getCached() == null ? 2 : asyncModel.getCached());
            //计数
            kafkaService.sendCountMessage(JsonUtil.writeValueAsString(countModel));
            // 发送计费MQ
            /*if (apiInfo.getCharge() == 1) {
                rabbitMQService.chargeSend(JsonUtil.writeValueAsString(asyncModel));
            }*/
            // 存MONGO
            if (apiInfo.getSaveMongoDB() != null && ConstantEnum.YesNo.yes.getValue() == apiInfo.getSaveMongoDB()) {
                MongoRequest mongoRequest = new MongoRequest();
                mongoRequest.setDbName(apiInfo.getMongodbDBName());
                mongoRequest.setCollectionName(apiInfo.getMongodbCollectionName());
                mongoRequest.setAsyncModel(asyncModel);
                rabbitMQService.convertAndSend(JsonUtil.writeValueAsString(mongoRequest));
            }
            // 发送返回结果到配置的MQ
            if (apiInfo.getSaveMQ() != null && ConstantEnum.YesNo.yes.getValue() == apiInfo.getSaveMQ()) {
                Integer mqType = apiInfo.getMqType();
                if (1 == mqType) {
                    LOGGER.debug("发送mq");
                    rabbitMQService.convertAndSend(apiInfo.getMqTopicName(), JsonUtil.writeValueAsString(asyncModel));
                } else if (2 == mqType) {
                    LOGGER.debug("发送kafka,topic:{}", apiInfo.getMqTopicName());
                    kafkaService.send(apiInfo.getMqTopicName(), JsonUtil.writeValueAsString(asyncModel));
                    LOGGER.debug("end发送kafka,topic:{}", apiInfo.getMqTopicName());
                }
            }
        } catch (Exception e) {
            LOGGER.error("send mq error: {}", e.getMessage());
        }
    }
}
