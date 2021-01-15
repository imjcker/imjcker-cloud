package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiInfoWithSubApi;
import com.imjcker.manager.manage.po.ApiRateDistributeWithBLOBs;
import com.imjcker.manager.manage.po.BackendDistributeParams;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.SubApiWeightQuery;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.ApiRateDistribute;
import com.imjcker.manager.manage.service.SubUpStreamApiService;
import com.imjcker.manager.manage.validator.ApiValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author Lemon.kiana
 * @version 2.0
 *          2017年9月20日 上午10:21:12
 * @Title SubUpStreamApiController
 * @Description 子上游Api控制器
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Api(description = "子上游API")
@RequestMapping("/api/back/sub")
@RestController
public class SubUpStreamApiController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    SubUpStreamApiService subUpStreamApiService;

    @ApiOperation(value = "查询子及其api父api信息", notes = "更新子上游API")
    @PostMapping("/get")
    public CommonResult get(@RequestBody JSONObject jsonObject) {
        ApiRateDistributeWithBLOBs apiRateDistribute = jsonObject.toJavaObject(ApiRateDistributeWithBLOBs.class);
        if (apiRateDistribute.getId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUB_API_ID);
        if (apiRateDistribute.getApiId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_ID);

        return new CommonResult(ResultStatusEnum.SUCCESS, subUpStreamApiService.get(apiRateDistribute));
    }

    @ApiOperation(value = "更新子上游API", notes = "更新子上游API")
    @PostMapping("/save")
    public CommonResult save(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        //第三方子接口基本信息
        ApiRateDistributeWithBLOBs subThirdApiInfo = jsonObject.getObject("subThirdApiInfo", ApiRateDistributeWithBLOBs.class);
        //第三方子接口请求信息
        JSONArray subThirdRequestInfo = jsonObject.getJSONArray("subThirdRequestInfo");
//        //第三方子接口返回信息
//        JSONArray subThirdResultInfo = jsonObject.getJSONArray("subThirdResultInfo");

        if (subThirdApiInfo == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUB_THIRD_API_INFO);
//        if (CollectionUtil.isEmpty(subThirdResultInfo))
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUB_THIRD_RESULT_INFO);

        //校验基本信息
        ApiValidate.subApiCheck(subThirdApiInfo);

        List<BackendDistributeParams> subThirdRequestInfoList = Collections.EMPTY_LIST;
        if (CollectionUtil.isNotEmpty(subThirdRequestInfo)) {
            subThirdRequestInfoList = subThirdRequestInfo.toJavaList(BackendDistributeParams.class);
            ApiValidate.backendDistributeParamsCheckList(subThirdRequestInfoList);
        }

        int id = subUpStreamApiService.save(subThirdApiInfo, subThirdRequestInfoList);

        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }

    @ApiOperation(value = "更改名字", notes = "更改名字")
    @PostMapping("/update/name")
    public CommonResult updateApiName(@RequestBody JSONObject jsonObject) {

        //第三方子接口基本信息
        ApiRateDistribute apiRateDistribute = jsonObject.getObject("subThirdApiInfo", ApiRateDistribute.class);
        if (apiRateDistribute == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUB_THIRD_API_INFO);

        if (StringUtils.isBlank(apiRateDistribute.getInterfaceName()))
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_NAME);

        subUpStreamApiService.updateApiName(apiRateDistribute);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiRateDistribute);
    }


    @ApiOperation(value = "删除子上游API", notes = "删除子上游API")
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        ApiRateDistribute apiRateDistribute = jsonObject.toJavaObject(ApiRateDistribute.class);
        if (apiRateDistribute.getId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUB_API_ID);
        if (apiRateDistribute.getApiId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_ID);


        subUpStreamApiService.del(apiRateDistribute);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiRateDistribute);
    }


    @ApiOperation(value = "子接口查询入口", notes = "子接口查询入口")
    @PostMapping("/query")
    public CommonResult query(@RequestBody JSONObject jsonObject) {
        ApiInfoWithSubApiQuery query = jsonObject.toJavaObject(ApiInfoWithSubApiQuery.class);
        PageInfo<ApiInfoWithSubApi> pageInfo = subUpStreamApiService.query(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, pageInfo);

    }


    @ApiOperation(value = "查询子接口权重配置信息", notes = "查询子接口查询权重配置信息")
    @PostMapping("/weight/get/config")
    public CommonResult getWeightConfig(@RequestBody JSONObject jsonObject) {
        ApiRateDistribute apiRateDistribute = jsonObject.toJavaObject(ApiRateDistribute.class);

        if (apiRateDistribute.getApiId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_ID);

        List<SubApiWeightQuery> l = subUpStreamApiService.getWeightConfigs(apiRateDistribute);
        return new CommonResult(ResultStatusEnum.SUCCESS, l);
    }

    @ApiOperation(value = "维护子接口权重配置信息", notes = "维护子接口权重配置信息")
    @PostMapping("/weight/update/config")
    public CommonResult updateWeightConfig(@RequestBody JSONObject jsonObject) {

        JSONArray weightConfigs = jsonObject.getJSONArray("weightConfigs");
        List<SubApiWeightQuery> weightConfigsList = weightConfigs.toJavaList(SubApiWeightQuery.class);
        List<SubApiWeightQuery> l = subUpStreamApiService.updateWeightConfigs(weightConfigsList);
        return new CommonResult(ResultStatusEnum.SUCCESS, l);
    }
}
