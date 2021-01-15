package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.ProfitQuery;
import com.imjcker.manager.charge.vo.ProfitDetailVO;
import com.imjcker.manager.charge.vo.ProfitVO;
import com.imjcker.manager.charge.service.ProfitService;
import com.imjcker.manager.charge.vo.*;
import com.lemon.common.util.DownloadUtils;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 查询利润
 */
@RestController
@RequestMapping("/profit")
public class ProfitController {
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径
    @Autowired
    private ProfitService profitService;

    /**
     * 利润显示，按照分组显示
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listProfit")
    public CommonResult listProfit(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        ProfitQuery query = jsonObject.toJavaObject(ProfitQuery.class);
        ProfitVO profitVO = profitService.listProfit(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, profitVO);
    }

    /**
     * 利润详情，按照分组下的接口
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "利润详情", notes = "利润详情")
    @PostMapping("/listProfitDetail")
    public CommonResult listBillDetail(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        ProfitQuery query = jsonObject.toJavaObject(ProfitQuery.class);
        ProfitDetailVO profitDetailVO = profitService.listProfitDatail(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, profitDetailVO);
    }

    /**
     * @param sourceName    分组名称
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param response
     */
    @ApiOperation(value = "利润导出", notes = "利润导出")
    @GetMapping(value = "profitExport")
    public void billExport(@RequestParam(name = "sourceName") String sourceName, @RequestParam(name = "startTime") String startTime,
                           @RequestParam(name = "endTime") String endTime, HttpServletResponse response) throws IOException {
        String filePath = FILE_PATH;
        String fileName = File.separator + "profit-"+sourceName + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        profitService.downloadProfit(sourceName, Long.valueOf(startTime), Long.valueOf(endTime), filePath, fileName);
        DownloadUtils.downloadFile(filePath + fileName + ".csv", fileName.substring(1) + ".csv", response);
    }


}
