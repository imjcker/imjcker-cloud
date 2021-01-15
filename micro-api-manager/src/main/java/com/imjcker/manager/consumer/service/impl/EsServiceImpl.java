package com.imjcker.manager.consumer.service.impl;

import com.imjcker.manager.consumer.service.EsService;
import com.imjcker.manager.elastic.elasticsearch.EsRestClient;
import com.imjcker.manager.elastic.model.CommonLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsServiceImpl implements EsService {
    @Autowired
    private EsRestClient esRestClient;

    @Override
    public void insert(String index, String type, CommonLogInfo info) {
        esRestClient.save(index,type,info.getUid(),info);
    }

    @Override
    public void update(String index, String type, CommonLogInfo info) {
        esRestClient.update(index,type,info.getUid(),info);
    }

    @Override
    public void insertAll(String index, String type, List<CommonLogInfo> list) {
        esRestClient.saveAll(index,type,list);
    }
}
