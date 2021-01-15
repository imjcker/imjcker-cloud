package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.model.CountApiByAppKeyVO;
import com.imjcker.manager.manage.model.ShowVO;
import com.imjcker.manager.elastic.model.AppKeyCount;
import com.imjcker.manager.elastic.model.AppKeyQuery;

import java.util.List;

public interface AppKeyCountService {
    ShowVO index(String index, String type, AppKeyQuery query);

    List<AppKeyCount> searchByApiId(String index, String type, AppKeyQuery query);

    List<AppKeyCount> test(String index, String type, AppKeyQuery query);

    CountApiByAppKeyVO countApiByAppKey(String index, String type, AppKeyQuery query);
}
