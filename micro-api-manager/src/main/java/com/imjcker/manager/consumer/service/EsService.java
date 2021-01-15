package com.imjcker.manager.consumer.service;

import com.imjcker.manager.elastic.model.CommonLogInfo;

import java.util.List;


public interface EsService {
    void insert(String indexName, String type, CommonLogInfo info);

    void update(String index, String type, CommonLogInfo info);

    void insertAll(String index, String type, List<CommonLogInfo> list);
}
