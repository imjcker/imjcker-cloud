package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.InterfaceCountModel;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class InterfaceCountService {

    private static DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    private MongoClient mongoClient;

    public List<JSONObject> handleResult(List<InterfaceCountModel> data, Long time, Integer timeUnit) throws Exception {
        List<JSONObject> result = new ArrayList<>();
        Map<Long, List<InterfaceCountModel>> apiMap = data.stream().collect(Collectors.groupingBy(InterfaceCountModel::getApiId));
        double realTime = timeTransfer(time, timeUnit);
        apiMap.forEach((k, v) -> {
            JSONObject temp = new JSONObject();
            int totalCount = v.size();
            OptionalDouble average = v.stream().mapToLong(InterfaceCountModel::getResponseTime).average();
            // 接口是否走缓存
            Map<Integer, List<InterfaceCountModel>> isCached = v.stream().collect(Collectors.groupingBy(InterfaceCountModel::getCached));
            // 接口是否调用成功
            Map<Integer, List<InterfaceCountModel>> resultFlag = v.stream().collect(Collectors.groupingBy(InterfaceCountModel::getResultStatus));
            temp.put("totalCount", totalCount);
            temp.put("responseTime", average.isPresent() ? (int) average.getAsDouble() : 0);
            temp.put("cachedData", isCached.containsKey(1) ? isCached.get(1).size() : 0);
            temp.put("successData", resultFlag.containsKey(1) ? resultFlag.get(1).size() : 0);
            temp.put("failData", resultFlag.containsKey(2) ? resultFlag.get(2).size() : 0);
            temp.put("frequency", df.format(totalCount / realTime));
            temp.put("apiName", v.get(0).getApiName());
            temp.put("apiId", k);
            result.add(temp);
        });
        return result;
    }

    public static double timeTransfer(Long time, Integer timeUnit) throws Exception {
        if (timeUnit == null) timeUnit = 1;
        double realTime;
        switch (timeUnit) {
            //秒
            case 1:
                realTime = time;
                break;
            //分
            case 2:
                realTime = time / 60.0;
                break;
            //时
            case 3:
                realTime = time / (60.0 * 60.0);
                break;
            default:
                realTime = time;
                break;
        }
        if (realTime == 0.0) {
            throw new Exception();
        }
        String format = df.format(realTime);
        return Double.parseDouble(format);
    }

    /**
     * 根据条件查找
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param apiId        apiId
     * @param appKey       appKey
     * @param cached       是否缓存
     * @param resultStatus 结果状态
     * @return 返回 InterfaceCountModel 列表, 用于计算图标展示结果.
     */
    public List<InterfaceCountModel> findByConditions(Long startTime, Long endTime, String apiId, String appKey, String cached, String resultStatus) {
        List<InterfaceCountModel> list = new ArrayList<>();
        BasicDBObject filter = new BasicDBObject();
        BasicDBObject dateFilter = new BasicDBObject();
        dateFilter.put("$lte", endTime + "");
        dateFilter.put("$gte", startTime + "");
        filter.put("createTime", dateFilter);
        if (StringUtils.isNotBlank(apiId)) {
            filter.put("apiId", Integer.parseInt(apiId));
        }
        if (StringUtils.isNotBlank(appKey)) {
            filter.put("appKey", appKey);
        }
        if (StringUtils.isNotBlank(resultStatus)) {
            filter.put("resultStatus", Integer.parseInt(resultStatus));
        }
        if (StringUtils.isNotBlank(cached)) {
            filter.put("cached", Integer.parseInt(cached));
        }
        MongoCollection<Document> mongoCollection = mongoClient.getDatabase("inmgrData").getCollection("ifaceData");//getCollection("inmgrData", "ifaceData");
        for (Document document : mongoCollection.find(filter)) {
            InterfaceCountModel t = JSONObject.parseObject(document.toJson(), InterfaceCountModel.class);
            list.add(t);
        }
        return list;
    }
}
