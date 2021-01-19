package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.vo.CommonResult;
import com.lemon.common.vo.CompanyApp;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 客户appKey管理类
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    /**
     * 客户列表页面
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/search")
    public CommonResult search(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = appService.search(jsonObject);
        return CommonResult.success( result);
    }

    /**
     * 删除(客户appKey删除，对应的合约也应该删除)
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty() || null == jsonObject.getInteger("id"))
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        if (!appService.delete(jsonObject.getInteger("id"))) {
            return CommonResult.error();
        }
        return CommonResult.success();
    }

    /**
     * 新增
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @PostMapping("/save")
    public CommonResult save(@RequestBody JSONObject jsonObject) throws ParseException {
        if (!appService.save(jsonObject)) {
            return CommonResult.error();
        }
        return CommonResult.success();
    }

    /**
     * 编辑(只允许编辑客户描述和客户名称，appKey不允许修改，因为修改appKey后，已经建立的合约就会存在合约但是客户不存在的情况)
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @PostMapping("/edit")
    public CommonResult edit(@RequestBody JSONObject jsonObject) throws ParseException {
        if (!appService.edit(jsonObject)) {
            return CommonResult.error();
        }
        return CommonResult.success();
    }
    /**
     * 客户列表页面
     *
     * @param
     * @return
     */
    @PostMapping("/findApp")
    public CommonResult findApp() {
        List<CompanyApp> result = appService.findApp();
        return CommonResult.success( result);
    }

}
