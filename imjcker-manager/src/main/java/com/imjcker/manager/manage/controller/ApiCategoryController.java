package com.imjcker.manager.manage.controller;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.vo.ApiCategoryQuery;
import com.imjcker.manager.manage.vo.ApiVO;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.ApiCategory;
import com.imjcker.manager.manage.service.ApiCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(description = "接口领域分类操作")
@RequestMapping("/apiCategory")
public class ApiCategoryController {
    private final ApiCategoryService apiCategoryService;

    public ApiCategoryController(ApiCategoryService apiCategoryService) {
        this.apiCategoryService = apiCategoryService;
    }

    @PostMapping("save")
    @ApiOperation("保存领域分类")
    public CommonResult<Boolean> save(@RequestBody ApiCategory apiCategory) {
        if (apiCategoryService.insert(apiCategory)) {
            return CommonResult.success();
        } else {
            return CommonResult.fail(ResultStatusEnum.ERROR);
        }
    }

    @PostMapping("update")
    @ApiOperation("更新领域分类")
    public CommonResult<Boolean> update(@RequestBody ApiCategory apiCategory) {
        Assert.notNull(apiCategory.getId(), "ID不能为空");

        if (apiCategoryService.update(apiCategory)) {
            return CommonResult.success();
        } else {
            return CommonResult.fail(ResultStatusEnum.ERROR);
        }
    }

    @PostMapping("delete")
    @ApiOperation("删除领域分类")
    public CommonResult<Boolean> delete(@RequestBody ApiCategory apiCategory) {
        Assert.notNull(apiCategory.getId(), "ID不能为空");

        if (apiCategoryService.delete(apiCategory)) {
            return CommonResult.success();
        } else {
            return CommonResult.fail(ResultStatusEnum.ERROR);
        }
    }

    @PostMapping("findForPage")
    @ApiOperation("领域分类分页查询")
    public CommonResult<PageInfo> findForPage(@RequestBody ApiCategoryQuery apiCategory) {
        return CommonResult.success(apiCategoryService.findForPage(apiCategory));
    }

    @PostMapping("findAll")
    @ApiOperation("查询所有领域分类")
    public CommonResult<List<ApiCategory>> findAll() {
        return CommonResult.success(apiCategoryService.findAll());
    }

    @PostMapping("findApiByCategory")
    @ApiOperation("查询所有领域分类")
    public CommonResult<List<ApiVO>> findApiByCategory(@RequestBody ApiCategory apiCategory) {
        Assert.notNull(apiCategory.getId(), "分类ID不能为空");

        return CommonResult.success(apiCategoryService.findApiByCategory(apiCategory.getId()));
    }
}
