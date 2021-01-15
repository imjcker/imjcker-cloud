package com.imjcker.manager.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.vo.CompanyApp;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AppService {

    boolean delete(Integer id);

    Map<String,Object> search(JSONObject jsonObject);

    boolean save(JSONObject jsonObject) throws ParseException;

    boolean edit(JSONObject jsonObject) throws ParseException;

    List<CompanyApp> findApp();

}
