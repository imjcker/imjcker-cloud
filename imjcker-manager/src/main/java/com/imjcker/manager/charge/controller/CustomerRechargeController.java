package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.CompanyAppParam;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.imjcker.manager.charge.po.CompanyAppParam;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.service.CustomerRechargeService;
import com.imjcker.manager.charge.vo.request.ReqAppkeyByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author WT
 * @Date 10:30 2020/2/24
 * @Version CustomerRechargeController v1.0
 * @Desicrption 客户充值
 */
@RestController
@RequestMapping("/customer")
public class CustomerRechargeController {

    @Autowired
    private CustomerRechargeService customerRechargeService;

    /**
     * @Description : 分页 查询客户列表,带余额
     * @Return : com.lemon.common.vo.CommonResult
     * @Date : 2020/2/24 10:40
     */
    @RequestMapping("/list")
    public CommonResult queryCustomer(@RequestBody JSONObject jsonObject) {
        CompanyAppParam appParam = jsonObject.toJavaObject(CompanyAppParam.class);
        PageInfo<CompanyAppsVo> pageInfo = customerRechargeService.queryCustomer(appParam);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageInfo.getTotal());
        data.put("list", pageInfo.getList());
        return new CommonResult(ResultStatusEnum.SUCCESS,data);
    }

    @RequestMapping("/add")
    public CommonResult addCustomer(@RequestBody JSONObject jsonObject) {
        CompanyApps app = jsonObject.toJavaObject(CompanyApps.class);
        if (null == app || StringUtils.isBlank(app.getAppKey())) {
            return new CommonResult(ResultStatusEnum.ERROR, "appKey 不能为空");
        }
        KeyPairGenerator general = null;
        try {
            general = KeyPairGenerator.getInstance("RSA");
        } catch (Exception e) {
            throw new DataValidationException("生成公钥失败");
        }
        general.initialize(1024);
        KeyPair keyPair = general.generateKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        app.setPrivateKey(new String(Base64.encodeBase64(aPrivate.getEncoded())));
        app.setPublicKey(new String(Base64.encodeBase64(aPublic.getEncoded())));
        customerRechargeService.addCustomer(app);
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    @RequestMapping("/update")
    public CommonResult updateCustomer(@RequestBody JSONObject jsonObject) {
        CompanyApps app = jsonObject.toJavaObject(CompanyApps.class);
        customerRechargeService.updateCustomer(app);
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    @RequestMapping("/delete")
    public CommonResult deleteCustomer(@RequestBody JSONObject jsonObject) {
        CompanyApps companyApps = jsonObject.toJavaObject(CompanyApps.class);
        customerRechargeService.deleteCustomer(companyApps);
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    /**
     * @Description : 客户充值
     * @param jsonObject
     * @Return : com.lemon.common.vo.CommonResult
     * @Date : 2020/2/24 12:09
     */
    @RequestMapping("/recharge")
    public CommonResult recharge(@RequestBody JSONObject jsonObject) {
        CompanyAppsRecharge appRecharge = jsonObject.toJavaObject(CompanyAppsRecharge.class);
        if (StringUtils.isBlank(appRecharge.getAppKey()))
            return new CommonResult(ResultStatusEnum.ERROR, "appKey 不能为空");
        if (appRecharge.getAmount() == null)
            return new CommonResult(ResultStatusEnum.ERROR, "充值金额不能为空");
        customerRechargeService.charge(appRecharge);
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    /**
     * @Description : 分页查询 充值记录
     * @param jsonObject
     * @Return : com.lemon.common.vo.CommonResult
     * @Date : 2020/2/24 14:18
     */
    @RequestMapping("/chargeRecord")
    public CommonResult rechargeRecord(@RequestBody JSONObject jsonObject) {
        CompanyAppParam appParam = jsonObject.toJavaObject(CompanyAppParam.class);
        PageInfo<CompanyAppsRecharge> appRechargePageInfo = customerRechargeService.queryChargeRecord(appParam);
        Map<String, Object> data = new HashMap<>();
        data.put("total", appRechargePageInfo.getTotal());
        data.put("list", appRechargePageInfo.getList());
        return new CommonResult(ResultStatusEnum.SUCCESS, data);
    }

    @PostMapping("/balance/history")
    public CommonResult balanceHistory(@RequestBody @Valid ReqAppkeyByPage req){
        PageInfo<RespCompanyBalanceHistory> list = customerRechargeService.queryBalancePage(req);
        Map<String, Object> data = new HashMap<>();
        data.put("total", list.getTotal());
        data.put("list", list.getList());
        return new CommonResult(ResultStatusEnum.SUCCESS, data);
    }


}
