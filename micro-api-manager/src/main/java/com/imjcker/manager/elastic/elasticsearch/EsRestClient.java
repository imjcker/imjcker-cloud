package com.imjcker.manager.elastic.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.elastic.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ztzh_tanhh 2019/12/11
 **/
@Slf4j
@Data
public class EsRestClient {
    private HttpHost[] httpHosts;
    private static int size = 10000;

    public EsRestClient(HttpHost[] httpHosts) {
        this.httpHosts = httpHosts;
    }


    /**
     * 保存请求（id由es分配）
     *
     * @param index
     * @param type
     * @param doc
     * @throws IOException
     */
    public void save(String index, String type, Object doc) {
        save(index, type, null, doc);
    }

    /**
     * 保存请求(id由uid确定)
     *
     * @param index
     * @param type
     * @param id
     * @param doc
     * @throws IOException
     */
    public void save(String index, String type, String id, Object doc) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        IndexRequest request;
        if (StringUtils.isEmpty(id)) {
            request = new IndexRequest(index, type);
        } else {
            request = new IndexRequest(index, type, id);
        }
        String jsonString = JSONObject.toJSONString(doc);
        request.source(jsonString, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = restClient.index(request);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("关闭ES restClient 异常！{}", e.getMessage());
            }
        }
    }

    /**
     * 测试使用-根据field字段搜索数据
     *
     * @param indexName
     * @param type
     * @param field
     * @throws IOException
     */
    public String get(String indexName, String type, String field) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        String result = null;
        try {
            GetRequest getRequest = new GetRequest(indexName, type, field);
            GetResponse response = restClient.get(getRequest);
            result = response.getSourceAsString();
            log.debug("get-result: {}", result);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            }
        }
        return result;

    }

    /**
     * @param indexName
     * @param content
     * @param sort
     * @return
     * @throws IOException
     */
    public List<Object> list(String indexName, String content, String sort) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        List<Object> list = new ArrayList<>();
        try {
            //创建搜索请求
            SearchRequest request = new SearchRequest(indexName);
            //创建搜索构建者
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //构建搜索属性
            sourceBuilder.sort(new FieldSortBuilder(sort).order(SortOrder.ASC));//按照sort字段升序排序
            if (!StringUtils.isEmpty(content)) {
                //组合匹配条件
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                //创建bool匹配条件
                MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery(content, content).fuzziness(Fuzziness.AUTO);
                //将构造的匹配条件添加到bool条件中，must表示必须满足的条件，should表示设置加分项
                boolQueryBuilder.must(queryBuilder);
                sourceBuilder.query(boolQueryBuilder);
            }
            request.source(sourceBuilder);
            SearchResponse searchResponse = restClient.search(request, null);
            log.debug("status={}", searchResponse.status());

            SearchHits hits = searchResponse.getHits();
            hits.forEach(item -> {
                list.add(JSON.parse(item.getSourceAsString()));
            });
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            }
        }
        return list;
    }

    /**
     * 根据apiId统计接口调用总量-成功量-失败量-平均响应时间
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public Map<Integer, ApiCount> countByApiId(String indexName, String type, SourceQuery query) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
//        RestClient restClient = RestClient.builder(this.httpHosts).build();
//        Response response=restClient.performRequest("",null,null,null,new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024*1024*1024))
//        response.getEntity();
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(query, false);
        sourceBuilder.query(boolQueryBuilder);
        //聚合条件collectMode()
        TermsAggregationBuilder one = AggregationBuilders.terms("countApiId").field("apiId").size(size)
                .subAggregation(AggregationBuilders.terms("sourceResponseCode").field("sourceResponseCode").size(size)
                        .subAggregation(AggregationBuilders.avg("avgSpendTime").field("sourceSpendTime")).size(size));
        sourceBuilder.aggregation(one);
        Map<Integer, ApiCount> result = new HashMap<>();
        try {
            SearchResponse searchResponse = restClient.search(request);
//            Response response = restClient.per
            ;
            Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
            ParsedLongTerms parsedStringTerms = (ParsedLongTerms) aggMap.get("countApiId");
            for (Terms.Bucket groupByApiIdBucket : parsedStringTerms.getBuckets()) {
                int apiId = Integer.valueOf(String.valueOf(groupByApiIdBucket.getKey()));
                ApiCount temp = result.get(apiId);
                if (null == temp) {
                    temp = new ApiCount();
                    temp.setCount(groupByApiIdBucket.getDocCount());//接口总调用量
                }
                Map<String, Aggregation> codeMap = groupByApiIdBucket.getAggregations().asMap();
                ParsedLongTerms codeTerms = (ParsedLongTerms) codeMap.get("sourceResponseCode");
                for (Terms.Bucket codeBuccket1 : codeTerms.getBuckets()) {
                    Avg avg = (Avg) codeBuccket1.getAggregations().asMap().get("avgSpendTime");
/*                    log.info("{}接口总计次数{},其中返回码值为{}的请求数为{},平均响应时间为{}ms", groupByApiIdBucket.getKey()
                            , groupByApiIdBucket.getDocCount(), codeBuccket1.getKey(), codeBuccket1.getDocCount(), (int) avg.getValue());*/
                    //根据接口请求状态，分别计算成功量-失败量-异常量,平均响应时间
                    if (200 == (long) codeBuccket1.getKey()) {
                        temp.setCountSuccess(codeBuccket1.getDocCount());
                        temp.setAvgSuccessResponseTime((int) avg.getValue());
                    } else if (9999 == (long) codeBuccket1.getKey()) {
                        temp.setCountExp(temp.getCountExp() + codeBuccket1.getDocCount());
                    } else {
                        temp.setCountFail(temp.getCountFail() + codeBuccket1.getDocCount());
                    }
                }
                result.put(apiId, temp);
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
     * 根据分组名称统计分组调用总量-成功量-失败量-异常量-成功量的平均响应时间（筛选条件为：分组名称、时间段）
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public Map<String, GroupCount> countBySourceName(String indexName, String type, SourceQuery query) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(query, false);
        sourceBuilder.query(boolQueryBuilder);
        //聚合条件
        TermsAggregationBuilder one = AggregationBuilders.terms("sourceName").field("sourceName.keyword")
                .subAggregation(AggregationBuilders.terms("countApiId").field("apiId")
                        .subAggregation(AggregationBuilders.terms("sourceResponseCode").field("sourceResponseCode")
                                .subAggregation(AggregationBuilders.avg("avgSpendTime").field("sourceSpendTime")).size(size)));
        sourceBuilder.aggregation(one);
        Map<String, GroupCount> result = new HashMap<>();
        try {
            SearchResponse searchResponse = restClient.search(request);
            Map<String, Aggregation> sourceNameMap = searchResponse.getAggregations().asMap();
            ParsedStringTerms sourceNameTerm = (ParsedStringTerms) sourceNameMap.get("sourceName");
            for (Terms.Bucket sourceNameBucket : sourceNameTerm.getBuckets()) {
                Map<String, Aggregation> apiIdMap = sourceNameBucket.getAggregations().asMap();
                String sourceName = (String) sourceNameBucket.getKey();
                long sumTime = 0;//分组下每个接口成功调用量的平均响应时间的总和，用于计算分组的平均响应时间
                GroupCount temp = result.get(sourceName);
                if (null == temp) {
                    temp = new GroupCount();
                    temp.setSourceName(sourceName);//分组名称
                    temp.setCountBySourceName(sourceNameBucket.getDocCount());//分组总调用量
                }
                ParsedLongTerms apiIdTerms = (ParsedLongTerms) apiIdMap.get("countApiId");
                for (Terms.Bucket apiIdBucket : apiIdTerms.getBuckets()) {
                    Map<String, Aggregation> codeMap = apiIdBucket.getAggregations().asMap();
                    ParsedLongTerms codeTerms = (ParsedLongTerms) codeMap.get("sourceResponseCode");
                    for (Terms.Bucket codeBuccket : codeTerms.getBuckets()) {
                        Avg avg = (Avg) codeBuccket.getAggregations().asMap().get("avgSpendTime");
                        /*log.info("分组:{},分组总量:{},{}接口总计次数{},其中返回码值为{}的请求数为{},平均响应时间为{}ms",
                                sourceNameBucket.getKey(), sourceNameBucket.getDocCount(), apiIdBucket.getKey()
                                , apiIdBucket.getDocCount(), codeBuccket.getKey(), codeBuccket.getDocCount(), (int) avg.getValue());*/
                        if (200 == (long) codeBuccket.getKey()) {
                            temp.setCountSuccessBySourceName(codeBuccket.getDocCount() + temp.getCountSuccessBySourceName());
                            sumTime = sumTime + apiIdBucket.getDocCount() * (int) avg.getValue();
                        } else if (9999 == (long) codeBuccket.getKey()) {
                            temp.setCountExceptionBySourceName(codeBuccket.getDocCount() + temp.getCountExceptionBySourceName());
                        } else {
                            temp.setCountFailBySourceName(codeBuccket.getDocCount() + temp.getCountFailBySourceName());
                        }
                    }
                    temp.setAvgSuccessResponseTime((int) (sumTime / sourceNameBucket.getDocCount()));
                    result.put(sourceName, temp);
                }
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
     * 查询条件包装，返回boolQueryBuilder
     */
    private BoolQueryBuilder getBoolQueryBuilder(SourceQuery query, boolean codeFlag) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //        将构造的匹配条件添加到bool条件中，must表示必须满足的条件，should表示设置加分项
        if (0 != query.getApiId()) {
            TermQueryBuilder apiIdBuilder = QueryBuilders.termQuery("apiId", query.getApiId());
            boolQueryBuilder.must(apiIdBuilder);
        }
        if (!StringUtils.isEmpty(query.getSourceName())) {
            TermQueryBuilder apiIdBuilder = QueryBuilders.termQuery("sourceName.keyword", query.getSourceName());
            boolQueryBuilder.must(apiIdBuilder);
        }
        if (codeFlag) {//codeFlag为true，加入请求返回码值为条件,用于查询码值不为200的请求
            TermQueryBuilder codeBuilder = QueryBuilders.termQuery("sourceResponseCode", 200);
            boolQueryBuilder.mustNot(codeBuilder);
        }
        long startTime = query.getStartTime();
        long endTime = query.getEndTime();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("sourceCreateTime");
        rangeQueryBuilder.gte(startTime);
        rangeQueryBuilder.lte(endTime);
        boolQueryBuilder.filter(rangeQueryBuilder);
        return boolQueryBuilder;
    }

    /**
     * 根据uid更新数据（覆盖对应字段，若没有，新增字段到该uid数据中）
     *
     * @param index
     * @param type
     * @param uid
     * @param info
     */
    public void update(String index, String type, String uid, CommonLogInfo info) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        UpdateRequest updateRequest = new UpdateRequest(index, type, uid);
        updateRequest.docAsUpsert(true);
        updateRequest.doc(JSON.toJSONString(info), XContentType.JSON);
        try {
            UpdateResponse urb = restClient.update(updateRequest);
            RestStatus status = urb.status();
            log.debug("status={}", status.getStatus());
        } catch (IOException e) {
            log.error("ES更新出现异常:{}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("释放restClient失败:{}", e.getMessage());
            }
        }
    }

    /**
     * 插入数据
     */
    public void saveAll(String index, String type, List<CommonLogInfo> list) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(object -> {
            IndexRequest indexRequest = new IndexRequest(index, type, object.getUid());
            indexRequest.source(JSON.toJSONString(object), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulkResponse = restClient.bulk(bulkRequest, new BasicHeader("a", "3"));
            log.debug("bulkAdd:{}", JSON.toJSONString(bulkResponse));
        } catch (IOException e) {
            log.error("bulkAdd error,{}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("释放restClient失败:{}", e.getMessage());
            }
        }
    }

    /**
     * 保存请求(id由uid确定)
     *
     * @param index
     * @param type
     * @param id
     * @param doc
     * @throws IOException
     */
    public void saveAsync(String index, String type, String id, Object doc) {

        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        try {
            IndexRequest request;
            if (StringUtils.isEmpty(id)) {
                request = new IndexRequest(index, type);
            } else {
                request = new IndexRequest(index, type, id);
            }
            String jsonString = JSONObject.toJSONString(doc);
            request.source(jsonString, XContentType.JSON);
            ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    log.debug("异步返回结果{}", indexResponse.toString());
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("异步插入异常{}", e.getMessage());
                }
            };
            restClient.indexAsync(request, listener, new BasicHeader("1", "1"));

        } catch (Exception e) {
            log.error("saveAsync error: {}", e.getMessage());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("释放restClient失败:{}", e.getMessage());
            }
        }
    }

    /**
     * -----------------------------------------下游统计-----------------------------------------
     */

    /**
     * 根据appKey统计调用总量-成功量-失败量-异常量-成功量的平均响应时间（筛选条件为：分组名称、时间段）
     * Map的key值为appkey+apiId组合的字符串
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public List<AppKeyCount> countByAppKey(String indexName, String type, AppKeyQuery query) {
        List<AppKeyCount> result = new ArrayList<>();
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(query);
        sourceBuilder.query(boolQueryBuilder);
        //聚合条件
        TermsAggregationBuilder one = AggregationBuilders.terms("appKey").field("appKey.keyword").size(size)
                .subAggregation(AggregationBuilders.terms("countApiId").field("apiId").size(size)
                        .subAggregation(AggregationBuilders.terms("responseCode").field("responseCode").size(size)
                                .subAggregation(AggregationBuilders.avg("avgSpendTime").field("spendTime"))));
        sourceBuilder.aggregation(one);
        try {
            SearchResponse searchResponse = restClient.search(request);
            Map<String, Aggregation> appKeyMap = searchResponse.getAggregations().asMap();
            ParsedStringTerms appKeyTerm = (ParsedStringTerms) appKeyMap.get("appKey");
            //appKey循环
            for (Terms.Bucket appKeyBucket : appKeyTerm.getBuckets()) {
                AppKeyCount appKeyCount = new AppKeyCount();
                AtomicLong appKeyCountSuccess = new AtomicLong();
                AtomicLong appKeyCountFail = new AtomicLong();
                Map<String, Aggregation> apiIdMap = appKeyBucket.getAggregations().asMap();
                String appKey = (String) appKeyBucket.getKey();
                appKeyCount.setAppKey(appKey);
                appKeyCount.setCount(appKeyBucket.getDocCount());
                ParsedLongTerms apiIdTerms = (ParsedLongTerms) apiIdMap.get("countApiId");
                List<ApiIdCount> apiIdCountList = new ArrayList<>();
                //apiId循环
                for (Terms.Bucket apiIdBucket : apiIdTerms.getBuckets()) {
                    ApiIdCount apiIdCount = new ApiIdCount();
                    AtomicLong countFail = new AtomicLong();
                    Map<String, Aggregation> codeMap = apiIdBucket.getAggregations().asMap();
                    ParsedLongTerms codeTerms = (ParsedLongTerms) codeMap.get("responseCode");
                    apiIdCount.setApiId(Integer.valueOf(String.valueOf(apiIdBucket.getKey())));//接口id
                    apiIdCount.setCount(apiIdBucket.getDocCount());//接口总量
                    //不同状态循环
                    for (Terms.Bucket codeBuccket : codeTerms.getBuckets()) {
                        Avg avg = (Avg) codeBuccket.getAggregations().asMap().get("avgSpendTime");
                        log.debug("appKey:{},总量:{},{}接口调用次数{},码值为{}的请求次数={},平均响应时间为{}ms",
                                appKeyBucket.getKey(), appKeyBucket.getDocCount(), apiIdBucket.getKey()
                                , apiIdBucket.getDocCount(), codeBuccket.getKey(), codeBuccket.getDocCount(), (int) avg.getValue());
                        int code = Integer.valueOf(String.valueOf(codeBuccket.getKey()));
                        if (200 == code) {
                            apiIdCount.setCountSuccess(codeBuccket.getDocCount());//接口成功量
                            apiIdCount.setAvgSuccessSpendTime((int) avg.getValue());//成功的接口平均响应时间
                            appKeyCountSuccess.getAndAdd(codeBuccket.getDocCount());//appKey成功量统计
                        } else {
                            countFail.getAndAdd(codeBuccket.getDocCount());//统计除去状态码为200的接口失败量
                            appKeyCountFail.getAndAdd(codeBuccket.getDocCount());//appkey失败量统计
                        }
                    }
                    apiIdCount.setCountFail(countFail.get());//接口失败量
                    countFail.set(0);
                    apiIdCountList.add(apiIdCount);
                }
                appKeyCount.setApiCountList(apiIdCountList);
                appKeyCount.setCountSuccess(appKeyCountSuccess.get());
                appKeyCount.setCountFail(appKeyCountFail.get());
                appKeyCountSuccess.set(0);
                appKeyCountFail.set(0);
                result.add(appKeyCount);
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
     * 下游查询条件包装，返回boolQueryBuilder
     */
    private BoolQueryBuilder getBoolQueryBuilder(AppKeyQuery query) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //        将构造的匹配条件添加到bool条件中，must表示必须满足的条件，should表示设置加分项
        if (0 != query.getApiId()) {
            TermQueryBuilder apiIdBuilder = QueryBuilders.termQuery("apiId", query.getApiId());
            boolQueryBuilder.must(apiIdBuilder);
        }
        if (!StringUtils.isEmpty(query.getAppKey())) {
            TermQueryBuilder apiIdBuilder = QueryBuilders.termQuery("appKey.keyword", query.getAppKey());
            boolQueryBuilder.must(apiIdBuilder);
        }
        long startTime = query.getStartTime();
        long endTime = query.getEndTime();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("createTime");
        rangeQueryBuilder.gte(startTime);
        rangeQueryBuilder.lte(endTime);
        boolQueryBuilder.filter(rangeQueryBuilder);
        return boolQueryBuilder;
    }

    /**
     * 根据apiId统计接口调用总量-成功量-失败量-平均响应时间
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public Map<Integer, AppKeyCount> countByApiId(String indexName, String type, AppKeyQuery query) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(query);
        sourceBuilder.query(boolQueryBuilder);
        //聚合条件
        TermsAggregationBuilder one = AggregationBuilders.terms("countApiId").field("apiId").size(size)
                .subAggregation(AggregationBuilders.terms("responseCode").field("responseCode").size(size)
                        .subAggregation(AggregationBuilders.avg("avgSpendTime").field("spendTime")).size(size));
        sourceBuilder.aggregation(one);
        Map<Integer, AppKeyCount> result = new HashMap<>();
        try {
            SearchResponse searchResponse = restClient.search(request);
            Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
            ParsedLongTerms parsedStringTerms = (ParsedLongTerms) aggMap.get("countApiId");
            for (Terms.Bucket groupByApiIdBucket : parsedStringTerms.getBuckets()) {
                int apiId = Integer.valueOf(String.valueOf(groupByApiIdBucket.getKey()));
                AppKeyCount temp = result.get(apiId);
                if (null == temp) {
                    temp = new AppKeyCount();
                    temp.setCount(groupByApiIdBucket.getDocCount());//接口总调用量
                }
                Map<String, Aggregation> codeMap = groupByApiIdBucket.getAggregations().asMap();
                ParsedLongTerms codeTerms = (ParsedLongTerms) codeMap.get("responseCode");
                for (Terms.Bucket codeBuccket1 : codeTerms.getBuckets()) {
                    Avg avg = (Avg) codeBuccket1.getAggregations().asMap().get("avgSpendTime");
                    log.debug("{}接口总计次数{},其中返回码值为{}的请求数为{},平均响应时间为{}ms", groupByApiIdBucket.getKey()
                            , groupByApiIdBucket.getDocCount(), codeBuccket1.getKey(), codeBuccket1.getDocCount(), (int) avg.getValue());
                    //根据接口请求状态，分别计算成功量-失败量-异常量,平均响应时间
                    /*if (200 == (long) codeBuccket1.getKey()) {
//                        temp.setCountSuccessByAppKey(codeBuccket1.getDocCount());
//                        temp.setAvgSuccessResponseTime((int) avg.getValue());
                    } else {
//                        temp.setCountFailByAppKey(temp.getCountFailByAppKey() + codeBuccket1.getDocCount());
                    }*/
                }
                result.put(apiId, temp);
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
     * 获取失败请求列表
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public List<SourceLogInfo> queryErrorList(String indexName, String type, SourceQuery query, int size) {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(this.httpHosts));
        SearchRequest request = new SearchRequest(indexName);
        request.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(query, true);
        sourceBuilder.query(boolQueryBuilder);
        List<SourceLogInfo> result = new ArrayList<>();
        try {
            SearchResponse searchResponse = restClient.search(request);
            SearchHits hits = searchResponse.getHits();
            if (hits != null && hits.totalHits > 0) {
                hits.forEach(item -> {
                    SourceLogInfo info = JSON.parseObject(item.getSourceAsString(), SourceLogInfo.class);
                    result.add(info);
                });
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
     * 根据apiId统计接口调用总量-成功量-失败量-平均响应时间
     *
     * @param indexName
     * @param type
     * @param query
     * @return
     */
    public Map<Integer, ApiCount> countByApiIdTest(String indexName, String type, SourceQuery query) throws IOException {
        RestClient restClient = RestClient.builder(this.httpHosts).build();
        String mothod = "GET";
        String endpoint = "/" + indexName + "/" + type + "/" + "_search";
//        HttpEntity entity = new NStringEntity("{\n" +
//                "  \"aggregations\":{\n" +
//                "    \"apiId\":{\n" +
//                "      \"aggregations\":{\n" +
//                "        \"sourceResponsecode\":{\n" +
//                "          \"aggregations\":{\n" +
//                "            \"AVG(sourceSpendtime)\":{\n" +
//                "              \"avg\":{\n" +
//                "                \"field\":\"sourceSpendtime\"\n" +
//                "              }\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"terms\":{\n" +
//                "            \"field\":\"sourceResponsecode\",\n" +
//                "            \"size\":10000\n" +
//                "          }\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"terms\":{\n" +
//                "        \"field\":\"apiId\",\n" +
//                "        \"size\":\"10000\"\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}", ContentType.APPLICATION_JSON);
        HttpEntity entity = new NStringEntity("{\"query\" : {\"bool\" : {\"must\": [{\"match_all\" : {}}]}},\"size\" : 0,\"aggregations\" : {\"apiId\":{\"aggregations\":{\"sourceResponseCode\":{\"aggregations\":{\"AVG(sourceSpendTime)\":{\"avg\":{\"field\":\"sourceSpendTime\"}}},\"terms\":{\"field\":\"sourceResponseCode\",\"size\":10000}}},\"terms\":{\"field\":\"apiId\",\"size\":\"1\"}}}}", ContentType.APPLICATION_JSON);
//        Response response = restClient.performRequest(mothod, endpoint, Collections.emptyMap(), entity, new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024));
        Response response = restClient.performRequest(mothod, endpoint, Collections.emptyMap(), entity, new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024));
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        restClient.close();
        log.debug("{}", jsonObject.toJSONString());
        return null;
    }
}
