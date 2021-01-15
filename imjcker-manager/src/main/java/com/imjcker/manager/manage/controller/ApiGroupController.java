package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.ApiGroup;
import com.imjcker.manager.manage.service.ApiGroupService;
import com.imjcker.manager.manage.service.ApiService;
import com.imjcker.manager.manage.validator.GroupValidate;
import com.imjcker.manager.manage.vo.ApiGroupQuery;
import com.imjcker.manager.manage.vo.MultiLevelGroup;
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

/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月12日 下午4:15:53
 * @Title ApiGroupController
 * @Description API分组控制器
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Api(description = "开放API-分组管理")
@RequestMapping("/apiGroup")
@RestController
public class ApiGroupController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    ApiGroupService apiGroupService;

    @Autowired
    ApiService apiService;

    /**
     * ①返回所有分组列表；②搜索单个分组模糊匹配
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "查询分组", notes = "查询分组")
    @PostMapping("list")
    public CommonResult list(@RequestBody JSONObject jsonObject) {
        ApiGroupQuery apiGroupQuery = jsonObject.toJavaObject(ApiGroupQuery.class);
        PageInfo<ApiGroup> apiGroupPageInfo = apiGroupService.findPageInfo(apiGroupQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiGroupPageInfo);
    }

    /**
     * 一次返回所有分组信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "所有分组", notes = "所有分组")
    @PostMapping("allList")
    public CommonResult allList() {
        List<ApiGroup> apiGroupList = apiGroupService.findAll();
        return new CommonResult(ResultStatusEnum.SUCCESS, apiGroupList);
    }

    /**
     * 保存新建分组
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "新建分组", notes = "新建分组")
    @PostMapping("save")
    public CommonResult save(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        ApiGroup apiGroup = jsonObject.toJavaObject(ApiGroup.class);
        //参数校验
        GroupValidate.GroupCheck(apiGroup);
        Integer id = apiGroupService.save(apiGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }

    /**
     * 更新分组
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "更新分组", notes = "更新分组")
    @PostMapping("update")
    public CommonResult update(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        ApiGroup apiGroup = jsonObject.toJavaObject(ApiGroup.class);
        //参数校验（UUID）
        GroupValidate.UUIDCheck(apiGroup);
        //参数校验
        GroupValidate.GroupCheck(apiGroup);
        apiGroupService.update(apiGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 删除分组
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "删除分组", notes = "删除分组")
    @PostMapping("logicDelete")
    public CommonResult logicDelete(@RequestBody JSONObject jsonObject) {
        String groupUUID = String.valueOf(jsonObject.get("groupUUID"));
        if (StringUtils.isBlank(groupUUID)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPUUID);
        }
        String message = apiGroupService.logicDelete(groupUUID);
        return new CommonResult(ResultStatusEnum.SUCCESS, message);
    }

    /**
     * 分组名称唯一性
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "分组名称唯一性", notes = "分组名称唯一性")
    @PostMapping("checkUnique")
    public CommonResult checkUnique(@RequestBody JSONObject jsonObject) {
        ApiGroupQuery apiGroupQuery = jsonObject.toJavaObject(ApiGroupQuery.class);
        boolean unique = apiGroupService.checkUnique(apiGroupQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }


    /**
     * 检查分组是否存在下级分组：true：不存在，可以挂分组和创建分组，false：存在，不可以挂分组
     *
     * @param jsonObject
     * @return
     * @Version 2.0
     */
    @ApiOperation(value = "检查分组下是否存在分组", notes = "检查分组下是否存在分组")
    @PostMapping("hasNoneOfLowerLevelGroup")
    public CommonResult hasNoneOfLowerLevelGroup(@RequestBody JSONObject jsonObject) {
        Integer groupId = Integer.parseInt(jsonObject.get("groupId").toString());
        boolean unique = apiGroupService.hasNoneOfLowerLevelGroup(groupId);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }

    @ApiOperation(value = "返回可以绑定API的分组", notes = "返回可以绑定API的分组")
    @PostMapping("listOfNoLowerLevelGroup")
    public CommonResult listOfNoLowerLevelGroup() {

        List<ApiGroup> groupList = apiGroupService.listOfNoLowerLevelGroup();
        if (groupList!=null && groupList.size()>0)
            return new CommonResult(ResultStatusEnum.SUCCESS, groupList);
        return new CommonResult(ResultStatusEnum.WARN, null);
    }

    @ApiOperation(value = "按层级结构返回分组", notes = "按层级结构返回分组")
    @PostMapping("listByLevel")
    public CommonResult listByLevel(@RequestBody JSONObject jsonObject) {

        ApiGroup apiGroup = jsonObject.toJavaObject(ApiGroup.class);
        List<MultiLevelGroup> multiLevelGroupList = apiGroupService.listByLevel(apiGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, multiLevelGroupList);
    }

    @ApiOperation(value = "按层级结构返回分组", notes = "按层级结构返回分组")
    @PostMapping("listByLevelForGroupUI")
    public CommonResult listByLevelForGroupUI(@RequestBody JSONObject jsonObject) {

        ApiGroup apiGroup = jsonObject.toJavaObject(ApiGroup.class);
        List<MultiLevelGroup> multiLevelGroupList = apiGroupService.listByLevel(apiGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, multiLevelGroupList);
    }

    @ApiOperation(value = "分页按层级结构返回分组", notes = "分页按层级结构返回分组")
    @PostMapping("pageInfoListByLevel")
    public CommonResult pageInfoListByLevel(@RequestBody JSONObject jsonObject) {

        ApiGroupQuery apiGroupQuery = jsonObject.toJavaObject(ApiGroupQuery.class);
        PageInfo<MultiLevelGroup> multiLevelGroupList = apiGroupService.pageInfoListByLevel(apiGroupQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, multiLevelGroupList);
    }

    /**
     * ①返回所有分组列表；②搜索单个分组模糊匹配
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "返回某一分组下的内容", notes = "返回某一分组下的内容")
    @PostMapping("listByParentId")
    public CommonResult listByParentId(@RequestBody JSONObject jsonObject) {

        ApiGroupQuery apiGroupQuery = jsonObject.toJavaObject(ApiGroupQuery.class);
        PageInfo<ApiGroup> apiGroupPageInfo = apiGroupService.listByParentIdPageInfo(apiGroupQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiGroupPageInfo);
    }
}
