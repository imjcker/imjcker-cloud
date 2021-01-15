package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author WT
 * @Date 11:11 2019/8/20
 * @Version CombInfService v1.0
 * @Desicrption
 */
public interface CombInfService {
    Map<String, String> executeCombInf(JSONObject json) throws Exception;
}
