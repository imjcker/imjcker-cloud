package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.CountApiByAppKeyVO;
import com.imjcker.manager.manage.model.ShowVO;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.AppKeyCountService;

import com.imjcker.manager.elastic.model.AppKeyCount;
import com.imjcker.manager.elastic.model.AppKeyQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 下游接口调用情况统计
 */
@RestController
@RequestMapping("/countByAppKey")
public class AppKeyCountControl {
    @Value("${es.indexZuul}")
    private String index;

    @Value("${es.typeZuul}")
    private String type;
    @Autowired
    private AppKeyCountService appKeyCountService;


    /**
     * 下游接口统计，维度----appKey，apiId，请求状态、响应时间、成功率
     */
    @PostMapping("/index")
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        AppKeyQuery query = jsonObject.toJavaObject(AppKeyQuery.class);
        ShowVO result = appKeyCountService.index(index, type, query);
        return CommonResult.success( result);
    }


    /**
     * 下游接口统计，维度----appKey对应接口，请求状态、响应时间、成功率
     */
    @PostMapping("/countApiByAppKey")
    public CommonResult countApiByAppKey(@RequestBody JSONObject jsonObject) {
        try {
            AppKeyQuery query = jsonObject.toJavaObject(AppKeyQuery.class);
            CountApiByAppKeyVO result = appKeyCountService.countApiByAppKey(index, type, query);
            return CommonResult.success( result);
        } catch (Exception e) {
            return CommonResult.ex(500, e.getMessage(), null);
        }
    }


    @PostMapping("/test")
    public CommonResult test(@RequestBody JSONObject jsonObject) {
        AppKeyQuery query = jsonObject.toJavaObject(AppKeyQuery.class);
        List<AppKeyCount> result = appKeyCountService.test(index, type, query);
        return CommonResult.success( result);
    }
}
