package com.imjcker.manager.manage.service;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.util.Map;

public interface WhiteIpListService {

    Map<String,Object> show(JSONObject jsonObject);

    boolean delete(JSONObject jsonObject);

    Map<String,Object> search(JSONObject jsonObject);

    boolean save(JSONObject jsonObject) throws ParseException;

    boolean edit(JSONObject jsonObject) throws ParseException;

    void syncRedis();
}
