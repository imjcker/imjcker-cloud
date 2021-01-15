package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.vo.response.RespBillingRules;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.service.BillingRulesService;
import com.imjcker.manager.charge.vo.response.RespBillingRules;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 计费规则管理
 */
@RestController
@RequestMapping("/billingRules")
@Api(description = "计费规则管理")
public class BillingRulesController {

    @Autowired
    private BillingRulesService billingRulesService;

    @PostMapping("/list")
    public CommonResult search(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        Map<String, Object> result = billingRulesService.list(jsonObject);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }
    /**
     * 删除
     * @param jsonObject
     * @return
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty() || null == jsonObject.getInteger("id"))
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        billingRulesService.delete(jsonObject.getInteger("id"));
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
     /* 新增
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody JSONObject jsonObject) {
        if (!billingRulesService.save(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/update")
    public CommonResult update(@RequestBody JSONObject jsonObject){
        if (jsonObject.isEmpty())
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        if (!billingRulesService.edit(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/chargeNameList")
    public CommonResult chargeNameList() {
        List<BillingRules> result = billingRulesService.chargeNameList();
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * @Description : uuid查billingRules对象
     * @Date : 2020/4/1 8:57
     * @Auth : qiuwen
     */
    @GetMapping("/byUuid")
    @ApiImplicitParam(paramType = "form")
    @ApiOperation(notes = "uuid查billingRules对象",value = "uuid查billingRules对象")
    public CommonResult<RespBillingRules> selectByUuid(@RequestParam String uuid){
        BillingRules billingRules = billingRulesService.selectByBillingRules(uuid);
        if(billingRules!=null){
            RespBillingRules respBillingRules = new RespBillingRules();
            BeanUtils.copyProperties(billingRules,respBillingRules);
            return new CommonResult(ResultStatusEnum.SUCCESS,respBillingRules);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }

}

