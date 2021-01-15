package com.imjcker.manager.charge.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.vo.response.RespBillingRules;

import java.util.List;
import java.util.Map;

public interface BillingRulesService {

    void delete(Integer id);

    Map<String,Object> list(JSONObject jsonObject);

    boolean save(JSONObject jsonObject);

    boolean edit(JSONObject jsonObject) ;

    List<BillingRules> chargeNameList();

    BillingRules selectByBillingRules(String uuid);
}
