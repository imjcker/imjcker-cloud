package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import com.imjcker.manager.charge.service.impl.CompareFlinkAndRedisJobServiceImpl;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.service.AuthService;
import com.imjcker.manager.charge.service.impl.CompareFlinkAndRedisJobServiceImpl;
import com.imjcker.manager.charge.vo.request.ReqAuthList;
import com.imjcker.manager.charge.vo.request.ReqStockByPage;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;

import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户接口权限管理类
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 列表页面
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/search")
    public CommonResult search(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        PageInfo<CompanyAppsAuthVo> search = authService.search(jsonObject);
        Map<String, Object> result = new HashMap<>();
        result.put("total", search.getTotal());
        result.put("list", search.getList());
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
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
        if (!authService.delete(jsonObject.getInteger("id"))) {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 新增
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @PostMapping("/save")
    public CommonResult save(@RequestBody JSONObject jsonObject) {

        if (!authService.save(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 编辑(只允许编辑计费方式,appKey不允许修改，因为修改appKey后，已经建立的合约就会存在合约但是客户不存在的情况)
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @PostMapping("/edit")
    public CommonResult edit(@RequestBody JSONObject jsonObject) throws ParseException {
        if (!authService.edit(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @PostMapping("/stock/history")
    public CommonResult stockHistory(@RequestBody ReqStockByPage req){
        PageInfo<RespAuthStockHistory> list = authService.queryStockPage(req);
        Map<String, Object> data = new HashMap<>();
        data.put("total", list.getTotal());
        data.put("list", list.getList());
        return new CommonResult(ResultStatusEnum.SUCCESS, data);
    }

    /**
     * @Description : 检测api是否绑定合约
     * @param apiId
     * @Return : com.lemon.common.vo.CommonResult<java.lang.Boolean>
     * @Date : 2020/4/16 16:10
     */
    @PostMapping("/checkApiAuth")
    public CommonResult<Boolean> checkApiAuth(@RequestParam("apiId") Integer apiId) {
        Boolean check = authService.checkApiAuth(apiId);
        return new CommonResult<>(ResultStatusEnum.SUCCESS, check);
    }

    /**
     * 获取客户加密key
     * @param appKey
     * @return
     */
    @GetMapping("/getSignKey")
    public CommonResult<RespCompanyKey> selectKey(@RequestParam("appKey") String appKey){
        RespCompanyKey companyAppsVo = authService.selectKey(appKey);
        return new CommonResult<>(ResultStatusEnum.SUCCESS,companyAppsVo);
    }

    @PostMapping("/listSave")
    public CommonResult listSave(@RequestBody @Valid ReqAuthList req){
        List<Integer> apiIds = req.getApiIds();
        if(apiIds==null || apiIds.size()==0){
            throw new BusinessException("请求接口ID不能为空");
        }
        for (Integer apiId : apiIds) {
            CompanyAppsAuth appsAuth = new CompanyAppsAuth();
            BeanUtils.copyProperties(req,appsAuth);
            appsAuth.setApiId(apiId);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(appsAuth);
            try {
                authService.save(jsonObject);
            } catch (BusinessException e){ }

        }
        return CommonResult.success();
    }

    /**
     * 运维接口 处理数据库密钥 本地使用放开注释
     */
    //@GetMapping("/qiuwen")
    public CommonResult updateForKey(){
        authService.updateForKey();
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    @Autowired
    private CompareFlinkAndRedisJobServiceImpl compareFlinkAndRedisJobService;

    /**
     * 对账测试
     */
    //@GetMapping("/job")
    public CommonResult jon(){
        compareFlinkAndRedisJobService.reconciliationFlinkBalance();
        return CommonResult.success();
    }
}
