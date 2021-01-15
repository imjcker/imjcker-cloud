package com.imjcker.spring.cloud.service.elastic.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.spring.cloud.service.elastic.model.Doc;
import com.imjcker.spring.cloud.service.elastic.model.DocNew;
import com.imjcker.spring.cloud.service.elastic.model.DocOld;
import com.imjcker.spring.cloud.service.elastic.repository.ElasticRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ElasticDao {
    private final ElasticRepository elasticRepository;

    public ElasticDao(ElasticRepository elasticRepository) {
        this.elasticRepository = elasticRepository;
    }

    public void saveDocOld(String index, String type, DocOld doc) {
        RestHighLevelClient client = getRestHighLevelClient();
        IndexRequest request = new IndexRequest(index, type, doc.getUuid());
        request.source(JSONObject.toJSONString(doc), XContentType.JSON);
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.error("关闭 restClient 异常。");
            }
        }
    }

    public void saveDocNew(String index, String type, DocNew doc) {
        RestHighLevelClient client = getRestHighLevelClient();
        IndexRequest request = new IndexRequest(index, type, doc.getUuid());

        request.source(JSONObject.toJSONString(doc), XContentType.JSON);
        try {
            IndexResponse re = client.index(request, RequestOptions.DEFAULT);
            log.info(re.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.error("关闭 restClient 异常。");
            }
        }
    }

    public void saveDoc(Doc doc) {
        elasticRepository.save(doc);
    }


    private RestHighLevelClient getRestHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new Node(new HttpHost("127.0.0.1", 9200))));
    }
}
