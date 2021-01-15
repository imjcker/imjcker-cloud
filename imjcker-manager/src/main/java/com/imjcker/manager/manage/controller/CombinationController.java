package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.po.ApiCombination;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.ApiCombinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangjingyi
 * @version 1.0
 * 2019年8月20日 上午11:05:59
 */
@Api(description = "组合接口列表")
@RequestMapping("/combination")
@RestController
public class CombinationController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApiCombinationService apiCombinationService;
    /**
     * 查询所属接口列表
     */
    @ApiOperation(value = "查询所属接口列表", notes = "查询所属接口列表")
    @PostMapping("query")
    public CommonResult query(@RequestBody JSONObject jsonObject) {
       Integer combinationId =  jsonObject.getInteger("combinationId");
       List<ApiCombination> list = apiCombinationService.selectById(combinationId);
       return new CommonResult(ResultStatusEnum.SUCCESS, list);
    }
    /**
     * 新增所属接口
     */
    @ApiOperation(value = "新增所属接口", notes = "新增所属接口")
    @PostMapping("add")
    public CommonResult add(@RequestBody JSONObject jsonObject) {
        ApiCombination apiCombination = jsonObject.toJavaObject(ApiCombination.class);
        Integer id = apiCombinationService.insert(apiCombination);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    /**
     * 编辑所属接口
     */
    @ApiOperation(value = "编辑所属接口", notes = "编辑所属接口")
    @PostMapping("update")
    public CommonResult update(@RequestBody JSONObject jsonObject) {
        ApiCombination apiCombination = jsonObject.toJavaObject(ApiCombination.class);
        Integer id = apiCombinationService.update(apiCombination);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    /**
     * 删除所属接口
     */
    @ApiOperation(value = "删除所属接口", notes = "删除所属接口")
    @PostMapping("delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        ApiCombination apiCombination = jsonObject.toJavaObject(ApiCombination.class);
        Integer id = apiCombinationService.delete(apiCombination);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
}
