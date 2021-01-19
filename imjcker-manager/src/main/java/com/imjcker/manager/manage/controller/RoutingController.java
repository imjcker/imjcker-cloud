package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.po.RoutingStrategy;
import com.imjcker.manager.manage.po.query.RoutingQuery;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.RoutingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by kangjingyi on 2019/4/1.
 * 路由策略
 */

@RestController
@RequestMapping("/routing")
public class RoutingController {

    @Resource
    private RoutingService routingService;

    /**新增*/
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public CommonResult addStrategy(@RequestBody JSONObject jsonObject) {
        RoutingStrategy newStrategy=jsonObject.toJavaObject(RoutingStrategy.class);
        newStrategy.setEnable("1");
        newStrategy.setRetryable("1");
        newStrategy.setStripPrefix("1");
        if(StringUtils.isBlank(newStrategy.getPath()) || StringUtils.isBlank(newStrategy.getaUrl()) ||StringUtils.isBlank(newStrategy.getName()))
            return new CommonResult(ResultStatusEnum.ERROR,null);
        //判断名称是否重复
        boolean flag = routingService.checkName(newStrategy);
        if (!flag) return new CommonResult(ResultStatusEnum.NAME_REPEAT, null);
        boolean pathFlag = routingService.checkPath(newStrategy);
        if (!pathFlag) return new CommonResult(ResultStatusEnum.PATH_REPEAT, null);
        routingService.addNew(newStrategy);
        return CommonResult.success();
    }
    /**删除*/
    @RequestMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        RoutingStrategy strategy=jsonObject.toJavaObject(RoutingStrategy.class);
        routingService.delete(strategy);
        return CommonResult.success();
    }
    /**更新*/
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult updateStrategy(@RequestBody JSONObject jsonObject) {
        RoutingStrategy updateStrategy=jsonObject.toJavaObject(RoutingStrategy.class);
        if(StringUtils.isBlank(updateStrategy.getPath()) || StringUtils.isBlank(updateStrategy.getaUrl()) ||StringUtils.isBlank(updateStrategy.getName()))
            return new CommonResult(ResultStatusEnum.ERROR,null);
        //判断名称是否重复
        boolean flag = routingService.checkName(updateStrategy);
        if (!flag) return new CommonResult(ResultStatusEnum.NAME_REPEAT, null);
        boolean pathFlag = routingService.checkPath(updateStrategy);
        if (!pathFlag) return new CommonResult(ResultStatusEnum.PATH_REPEAT, null);
        routingService.update(updateStrategy);
        return CommonResult.success();
    }
    /**
     * index查询
     * */
    @RequestMapping(value = "/index",method = RequestMethod.POST)
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        RoutingQuery query = jsonObject.toJavaObject(RoutingQuery.class);
        List<RoutingStrategy> list=routingService.queryByPage(query);
        query.setElements(list);
        return CommonResult.success(query);
    }
}
