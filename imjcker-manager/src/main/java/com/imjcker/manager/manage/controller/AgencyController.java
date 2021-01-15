package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.po.Agency;
import com.imjcker.manager.manage.po.query.AgencyQuery;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.impl.AgencyService;
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

import java.util.List;
import java.util.stream.Collectors;

@Api(description = "机构API接口列表")
@RequestMapping("/agency")
@RestController
public class AgencyController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AgencyService agencyService;

    @ApiOperation(value = "机构API接口列表", notes = "机构API接口列表")
    @PostMapping("findAgencyInfoList")
    public CommonResult findAgencyInfoList(@RequestBody JSONObject jsonObject) {
        AgencyQuery agencyQuery = jsonObject.toJavaObject(AgencyQuery.class);
        List<Agency> agencyPageInfo = agencyService.agencyInfoList(agencyQuery);
        agencyQuery.setElements(agencyPageInfo);
        return new CommonResult(ResultStatusEnum.SUCCESS, agencyQuery);
    }
    /**删除*/
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        Agency agency=jsonObject.toJavaObject(Agency.class);
        agencyService.delete(agency);
        LOGGER.info("id={}删除成功",agency.getId());
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }

    /**新增*/
    @PostMapping("/add")
    @ApiOperation(value = "新建", notes = "新建")
    public CommonResult add(@RequestBody JSONObject jsonObject) {
        Agency agency=jsonObject.toJavaObject(Agency.class);
        String apiName = jsonObject.getString("apiName");
        Integer sourceFlag = jsonObject.getInteger("sourceFlag");
        if (0==sourceFlag && StringUtils.isNotBlank(apiName)){//防止前端在选择源接口时传递接口名称
            agency.setApiName(null);
            agency.setApiId(null);
        }
        //源校验
        Boolean isExit = agencyService.isSourceExit(agency);
        if (isExit)
            return new CommonResult(ResultStatusEnum.AGENCY_SOURCE,false);
        //校验
        List<Agency> list = agencyService.checkUnique(agency);
        if (list!=null && list.size()>0)
            return new CommonResult(ResultStatusEnum.AGENCY_EXITE,false);
        agencyService.add(agency);
        LOGGER.info("appKey={},sourceFlag={},apiGroupName={},apiName={},dataConfig={}添加成功", agency.getAppKey(),agency.getSourceFlag(),agency.getApiGroupName(),agency.getApiName(),agency.getDataConfig());
        return new CommonResult(ResultStatusEnum.SUCCESS,true);
    }

    /**更新*/
    @PostMapping("/update")
    @ApiOperation(value = "更新", notes = "更新")
    public CommonResult update(@RequestBody JSONObject jsonObject) {
        Agency agency=jsonObject.toJavaObject(Agency.class);
        String apiName = jsonObject.getString("apiName");
        Integer sourceFlag = jsonObject.getInteger("sourceFlag");
        if (0==sourceFlag && StringUtils.isNotBlank(apiName)){//防止前端在选择源接口时传递接口名称
            agency.setApiName(null);
            agency.setApiId(null);
        }

        //源校验
        Boolean isExit = agencyService.isSourceExit(agency);
        if (isExit)
            return new CommonResult(ResultStatusEnum.AGENCY_SOURCE,false);
        //校验
        List<Agency> list = agencyService.checkUnique(agency);
        list = list.stream().filter(agency1 -> !agency.getId().equals(agency1.getId())).collect(Collectors.toList());
        if (list!=null && list.size()>0){
            return new CommonResult(ResultStatusEnum.AGENCY_EXITE,false);
        }
        agencyService.update(agency);
        LOGGER.info("id={},dataConfig={}修改成功", agency.getId(),agency.getDataConfig());
        return new CommonResult(ResultStatusEnum.SUCCESS,true);
    }

}
