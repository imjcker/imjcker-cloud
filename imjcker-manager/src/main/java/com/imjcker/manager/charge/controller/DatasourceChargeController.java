package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.service.DatasourceChargeService;
import com.imjcker.manager.charge.vo.request.ReqCheckUpdateDatasource;
import com.imjcker.manager.charge.vo.request.ReqDatasourceList;
import com.imjcker.manager.vo.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ztzh_tanhh 2020/3/17
 * 数据源计费信息管理接口
 **/
@Slf4j
@RestController
@RequestMapping("/datasourceCharge")
public class DatasourceChargeController {
    private final DatasourceChargeService datasourceChargeService;

    @Autowired
    public DatasourceChargeController(DatasourceChargeService datasourceChargeService) {
        this.datasourceChargeService = datasourceChargeService;
    }

    /**
     * 分页查询数据源计费管理信息
     *
     * @param jsonObject 查询条件
     * @return 分页结果
     */
    @RequestMapping("/findForPage")
    public CommonResult findForPage(@RequestBody JSONObject jsonObject) {

        PageInfo<DatasourceCharge> datasourceChargeIPage = datasourceChargeService.selectForPage(jsonObject);

        return CommonResult.success(datasourceChargeIPage);
    }

    /**
     * add
     *
     * @param jsonObject 保存对象
     * @return 保存结果
     */
    @RequestMapping("/save")
    public CommonResult save(@RequestBody JSONObject jsonObject) {
        boolean insert = datasourceChargeService.save(jsonObject);
        return CommonResult.success(insert);
    }
    /**
     * update
     *
     * @param jsonObject 保存对象
     * @return 保存结果
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody ReqCheckUpdateDatasource jsonObject) {
        try {
            boolean insert = datasourceChargeService.update(jsonObject.getId());
            return CommonResult.success(insert);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.ex(500, e.getMessage(), e.getMessage());
        }
    }

    @PostMapping("/check")
    @ApiOperation(value = "核查修改计费带来的最大亏损情况")
    public CommonResult<Map<String,BigDecimal>> checkUpdateInfo(@RequestBody ReqCheckUpdateDatasource req){
        Map<String, BigDecimal> map = datasourceChargeService.checkUpdateInfo(req);
        if(map.size()==0){
            return CommonResult.success();
        }
        return new CommonResult<>(ResultStatusEnum.RATE_CHECK,map);
    }

    /**
     * delete
     *
     * @param jsonObject 删除对象
     * @return 操作结果
     */
    @RequestMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        boolean delete = datasourceChargeService.delete(jsonObject);
        return CommonResult.success(delete);
    }

    /**
     * 根据apiId查询
     *
     * @param jsonObject 对象
     * @return 操作结果
     */
    @RequestMapping("/getChargeByApiId")
    public String getChargeByApiId(@RequestBody JSONObject jsonObject) {
        DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId(jsonObject);
        JSONObject object = new JSONObject();
        if (datasourceCharge != null) {
            object.put("price", datasourceCharge.getPrice());
            object.put("chargeUuid", datasourceCharge.getBillingRulesUuid());
        }
        return object.toJSONString();
    }
    /**
     * 根据billingRulesUuid查询
     *
     * @param jsonObject 对象
     * @return 操作结果
     */
    @RequestMapping("/getDatasourceChargeByBillingRulesUuid")
    public List<DatasourceCharge> getDatasourceChargeByBillingRulesUuid(@RequestBody JSONObject jsonObject) {
        DatasourceCharge datasourceCharge = jsonObject.toJavaObject(DatasourceCharge.class);
        return datasourceChargeService.getDatasourceChargeByBillingRulesUuid(datasourceCharge.getBillingRulesUuid());
    }


    @PostMapping("/listSave")
    @ApiOperation(value = "批量新增数据源计费")
    public CommonResult listSave(@RequestBody @Valid ReqDatasourceList req){
        List<Integer> apiIds = req.getApiIds();
        if(apiIds==null || apiIds.size()==0){
            throw new BusinessException("请求接口ID不能为空");
        }
        for (Integer apiId : apiIds) {
            DatasourceCharge datasourceCharge = new DatasourceCharge();
            BeanUtils.copyProperties(req,datasourceCharge);
            datasourceCharge.setApiId(apiId);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(datasourceCharge);
            try {
                datasourceChargeService.save(jsonObject);
            } catch (BusinessException e) {
            }
        }
        return CommonResult.success();
    }

}
