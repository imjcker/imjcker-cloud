package com.imjcker.spring.cloud.service.elastic.api;

import com.imjcker.spring.cloud.service.elastic.utils.ElasticSearchPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/es")
@Slf4j
public class Source2CommonController {
    @Value("${es.index}")
    private String sourceIndex;
    @Value("${es.type}")
    private String sourceType;
    @Value("${es.index-new}")
    private String targetIndex;
    @Value("${es.type-new}")
    private String targetType;

    @RequestMapping("/doUpdate")
    public void doUpdate(Integer from, Integer size) {
        Assert.notNull(from, "from cannot be null");
        Assert.notNull(size, "size cannot be null");

        startTransport(sourceIndex + "_tmp", sourceType, targetIndex, targetType, from, size);
    }


    private void startTransport(String sourceIndex, String sourceType, String targetIndex, String targetType, int from, int size) {
        SearchRequest request = new SearchRequest(sourceIndex);
        request.types(sourceType);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        try {
            SearchResponse search = ElasticSearchPoolUtil.getClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            long totalHits = hits.getTotalHits();

            hits.forEach(searchHitFields -> {
                String sourceAsString = searchHitFields.getSourceAsString();

                UpdateRequest updateRequest = new UpdateRequest();
                updateRequest.index(targetIndex).type(targetType).doc(sourceAsString);
                try {
                    ElasticSearchPoolUtil.getClient().update(updateRequest, RequestOptions.DEFAULT);
                } catch (Exception e) {
                    log.error("update error: {}", e.getMessage());
                }
            });

            // 递归
            if (totalHits == size) {
                from += size;
                startTransport(sourceIndex, sourceType, targetIndex, targetType, from, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            log.error("数据操作异常");
            e1.printStackTrace();
        }


    }
}

