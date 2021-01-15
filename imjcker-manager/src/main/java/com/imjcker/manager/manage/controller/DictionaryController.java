package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.DictionaryItemQuery;
import com.imjcker.manager.manage.po.query.DictionaryQuery;
import com.imjcker.manager.manage.po.query.DictionaryTypeQuery;
import com.imjcker.manager.manage.vo.dictionary.DictValueMapping;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.DictionaryService;
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

/**
 * .
 * User: lxl
 * Date: 2017/10/13
 * Time: 14:01
 * Description:字典类的相关操作
 */
@Api(description = "字典类型 ")
@RequestMapping("/dict")
@RestController
public class DictionaryController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    SubUpStreamApiService subUpStreamApiService;

    @ApiOperation(value = "api码值映射查询", notes = "api码值映射查询")
    @PostMapping("/query/config")
    public CommonResult queryApiDict(@RequestBody JSONObject jsonObject) {
        ApiInfoWithSubApiQuery query = jsonObject.toJavaObject(ApiInfoWithSubApiQuery.class);
        PageInfo<ApiInfoWithDictionary> pageInfo = dictionaryService.queryApiDict(query);
        for(ApiInfoWithDictionary api : pageInfo.getList()){
            for(ApiRateDistributeWithDictionary subApi:api.getSubApis()){
                if(StringUtils.isNotBlank(subApi.getResponseTransParam())){
                    subApi.setMapping(JSONArray.parseArray(subApi.getResponseTransParam(), DictValueMapping.class));
                }
            }
        }

        return new CommonResult(ResultStatusEnum.SUCCESS, pageInfo);

    }

    @ApiOperation(value = "码值查询", notes = "码值查询")
    @PostMapping("/query/dictionary")
    public CommonResult queryDict(@RequestBody JSONObject jsonObject) {
        DictionaryQuery dictionary = jsonObject.toJavaObject(DictionaryQuery.class);
        PageInfo<DictionaryWithQuery> pageInfo = dictionaryService.queryDictionary(dictionary);
        return new CommonResult(ResultStatusEnum.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "码值映射查询", notes = "码值映射查询")
    @PostMapping("/query/item")
    public CommonResult queryDictItem(@RequestBody JSONObject jsonObject) {
        DictionaryItemQuery dictionary = jsonObject.toJavaObject(DictionaryItemQuery.class);
        PageInfo<DictionaryItemWithQuery> pageInfo = dictionaryService.queryDictionaryItem(dictionary);
        return new CommonResult(ResultStatusEnum.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "码值类型查询", notes = "码值类型查询")
    @PostMapping("/query/type")
    public CommonResult queryDictType(@RequestBody JSONObject jsonObject) {
        DictionaryTypeQuery dictionaryTypeQuery = jsonObject.toJavaObject(DictionaryTypeQuery.class);
        PageInfo<DictionaryType> pageInfo = dictionaryService.queryDictionaryType(dictionaryTypeQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, pageInfo);
    }


    @ApiOperation(value = "更新字典信息", notes = "更新字典信息")
    @PostMapping("/save/dictionary")
    public CommonResult saveDictionary(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        Dictionary dictionary = jsonObject.toJavaObject(Dictionary.class);

        if (dictionary == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_INFO);

        ApiValidate.checkDictionary(dictionary);
        int id = dictionaryService.saveOrUpateDictionary(dictionary);
        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }


    @ApiOperation(value = "删除字典", notes = "删除字典")
    @PostMapping("/delete/dictionary")
    public CommonResult delDictionary(@RequestBody JSONObject jsonObject) {
        Dictionary dictionary = jsonObject.toJavaObject(Dictionary.class);
        if (dictionary.getId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ID);
        dictionaryService.delDictionary(dictionary);
        return new CommonResult(ResultStatusEnum.SUCCESS, dictionary);
    }

    @ApiOperation(value = "更新字典类型信息", notes = "更新字典类型信息")
    @PostMapping("/save/type")
    public CommonResult saveDictionaryType(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        DictionaryType dictionaryType = jsonObject.toJavaObject(DictionaryType.class);

        if (dictionaryType == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_TYPE_INFO);
        //校验基本信息
        ApiValidate.checkDictionaryType(dictionaryType);
        int id = dictionaryService.saveOrUpateDictionaryType(dictionaryType);
        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }

    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @PostMapping("/delete/type")
    public CommonResult delDictionaryType(@RequestBody JSONObject jsonObject) {
        DictionaryType dictionaryType = jsonObject.toJavaObject(DictionaryType.class);
        if (dictionaryType.getId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_TYPE_ID);
        dictionaryService.delDictionaryType(dictionaryType);
        return new CommonResult(ResultStatusEnum.SUCCESS, dictionaryType);
    }


    @ApiOperation(value = "更新字典码值信息", notes = "更新字典码值信息")
    @PostMapping("/save/item")
    public CommonResult saveDictionaryItem(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        DictionaryItem dictionaryItem = jsonObject.toJavaObject(DictionaryItem.class);

        if (dictionaryItem == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ITEM_INFO);

        //校验基本信息
        ApiValidate.checkDictionaryItem(dictionaryItem);
        int id = dictionaryService.saveOrUpateDictionaryItem(dictionaryItem);
        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }


    @ApiOperation(value = "删除字典类型码值", notes = "字典类型码值")
    @PostMapping("/delete/item")
    public CommonResult delDictionaryItem(@RequestBody JSONObject jsonObject) {
        DictionaryItem dictionaryItem = jsonObject.toJavaObject(DictionaryItem.class);
        if (dictionaryItem.getId() == null)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ITEM_ID);
        dictionaryService.delDictionaryItem(dictionaryItem);
        return new CommonResult(ResultStatusEnum.SUCCESS, dictionaryItem);
    }


}
