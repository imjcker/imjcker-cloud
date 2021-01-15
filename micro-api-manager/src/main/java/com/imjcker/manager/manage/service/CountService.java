package com.imjcker.manager.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.IndexVO;
import com.imjcker.manager.manage.po.ApiCountVO;
import com.imjcker.manager.manage.vo.ShowVOSourceLogInfo;
import com.imjcker.manager.elastic.model.SourceQuery;

import java.util.List;

public interface CountService {

    IndexVO index(String index, String type, SourceQuery query);

    IndexVO searchBySourceName(String index, String type, SourceQuery sourceQuery);

    List<ApiCountVO> searchByApiId(String index, String type, SourceQuery sourceQuery);

    JSONObject getInfoByUid(String index, String type, String indexZuul, String typeZuul, String uid);

    String test();

    ShowVOSourceLogInfo errorListPage(String index, String type, SourceQuery sourceQuery, int size);

    List<String> queryLogByUid(JSONObject jsonObject);
}
