package com.imjcker.manager.elastic.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.elastic.model.ApiChargeCount;
import com.imjcker.manager.elastic.model.DatasourceChargeCount;
import com.imjcker.manager.elastic.model.SourceLogInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kjy 2020/03/17
 * 客户调用详情
 **/
@Slf4j
@Data
public class DatasourceBillClient {
    private HttpHost[] httpHosts;
    private static int size = 10000;

    public DatasourceBillClient(HttpHost[] httpHosts) {
        this.httpHosts = httpHosts;
    }

    /**
     * 聚合统计(sourceName-apiId-chargeUuid-price)
     * 分类统计出调用量
     */
    public List<DatasourceChargeCount> customerCount(String indexName, String type, long startTime, long endTime, String sourceName) {
        List<DatasourceChargeCount> result = new ArrayList<>();

        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(startTime, endTime, sourceName);//限定查询时间范围
        sourceBuilder.query(boolQueryBuilder);
        //聚合条件
        TermsAggregationBuilder one = AggregationBuilders.terms("sourceName").field("sourceName.keyword").size(size)
                .subAggregation(AggregationBuilders.terms("apiId").field("apiId").size(size)
                        .subAggregation(AggregationBuilders.terms("chargeUuid").field("chargeUuid.keyword").size(size)
                                .subAggregation(AggregationBuilders.terms("price").field("price").size(size)
                                )
                        ));
        sourceBuilder.aggregation(one);
        try {
            SearchResponse searchResponse = restClient.search(request);
            Map<String, Aggregation> sourceNameMap = searchResponse.getAggregations().asMap();
            ParsedStringTerms sourceNameTerm = (ParsedStringTerms) sourceNameMap.get("sourceName");
            //sourceNam循环
            for (Terms.Bucket sourceNameBucket : sourceNameTerm.getBuckets()) {
                List<ApiChargeCount> apiChargeCountList = new ArrayList<>();
                DatasourceChargeCount customerChargeCount = new DatasourceChargeCount();
                String sourceNameTemp = (String) sourceNameBucket.getKey();//sourceName
                customerChargeCount.setSourceName(sourceNameTemp);
                customerChargeCount.setCount(sourceNameBucket.getDocCount());
                Map<String, Aggregation> apiIdMap = sourceNameBucket.getAggregations().asMap();
                ParsedLongTerms apiIdTerms = (ParsedLongTerms) apiIdMap.get("apiId");
                //apiId循环
                for (Terms.Bucket apiIdBucket : apiIdTerms.getBuckets()) {
                    ApiChargeCount apiChargeCount = new ApiChargeCount();
                    Integer apiId = Integer.valueOf(String.valueOf(apiIdBucket.getKey()));//apiId
                    apiIdBucket.getDocCount();//apiId-总量
                    apiChargeCount.setApiId(apiId);
                    Map<String, Aggregation> chargeUuidMap = apiIdBucket.getAggregations().asMap();
                    ParsedStringTerms chargeUuidTerms = (ParsedStringTerms) chargeUuidMap.get("chargeUuid");
                    //chargeUuid循环
                    for (Terms.Bucket chargeUuidBuccket : chargeUuidTerms.getBuckets()) {
                        String chargeUuid = (String) chargeUuidBuccket.getKey();//计费规则
                        chargeUuidBuccket.getDocCount();//计费guize-调用量
                        apiChargeCount.setChargeUuid(chargeUuid);
                        Map<String, Aggregation> priceMap = chargeUuidBuccket.getAggregations().asMap();
                        ParsedDoubleTerms priceTerms = (ParsedDoubleTerms) priceMap.get("price");
                        //price循环
                        for (Terms.Bucket priceBuccket : priceTerms.getBuckets()) {
                            apiChargeCount.setPrice(BigDecimal.valueOf((double) priceBuccket.getKey()));//价格
                            apiChargeCount.setCount(priceBuccket.getDocCount());//价格-量
                            log.debug("sourceName:{},总量:{},{}接口调用次数{},计费uuid为{}的调用次数={}," + "价格={}的调用次数{}次",
                                    sourceNameBucket.getKey(), sourceNameBucket.getDocCount(),
                                    apiIdBucket.getKey(), apiIdBucket.getDocCount(),
                                    chargeUuidBuccket.getKey(), chargeUuidBuccket.getDocCount(),
                                    priceBuccket.getKey(), priceBuccket.getDocCount());
                        }
                    }
                    apiChargeCountList.add(apiChargeCount);
                }
                customerChargeCount.setCustomerApiChargelistCount(apiChargeCountList);
                result.add(customerChargeCount);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("释放restClient失败:{}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 获取时间段内全量数据
     */
    public List<SourceLogInfo> getQueryInfoList(String indexName, String type, long startTime, long endTime) {
        List<SourceLogInfo> result = new ArrayList<>();
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        //        将构造的匹配条件添加到bool条件中，must表示必须满足的条件，should表示设置加分项
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("sourceCreateTime");
        rangeQueryBuilder.gte(startTime);
        rangeQueryBuilder.lt(endTime);
        boolQueryBuilder.filter(rangeQueryBuilder);//限定查询时间范围
        sourceBuilder.query(boolQueryBuilder);
        try {
            SearchResponse response = restClient.search(request);
            SearchHits hits = response.getHits();
            hits.forEach(item -> {
                JSONObject jsonObject = (JSONObject) JSON.parse(item.getSourceAsString());
                result.add(jsonObject.toJavaObject(SourceLogInfo.class));
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("释放restClient失败:{}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 构造查询条件（时间范围内的isCharge=true的请求参与计费）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuilder(long startTime, long endTime, String sourceName) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //        将构造的匹配条件添加到bool条件中，must表示必须满足的条件，should表示设置加分项
        if (!StringUtils.isEmpty(sourceName)) {
            TermQueryBuilder sourceNameBuilder = QueryBuilders.termQuery("sourceName.keyword", sourceName);
            boolQueryBuilder.must(sourceNameBuilder);
        }

        TermQueryBuilder chargeBuilder = QueryBuilders.termQuery("chargeFlag", "true");//chargeFlag=true为计费条件
        boolQueryBuilder.must(chargeBuilder);
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("sourceCreateTime");
        rangeQueryBuilder.gte(startTime);
        rangeQueryBuilder.lt(endTime);
        boolQueryBuilder.filter(rangeQueryBuilder);//限定查询时间范围
        return boolQueryBuilder;
    }
}
