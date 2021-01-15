package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.DatasourceBillDayQuery;
import com.imjcker.manager.charge.vo.DatasourceBillDetailVO;
import com.imjcker.manager.charge.vo.DatasourceBillMonthVO;
import com.imjcker.manager.charge.vo.DatasourceBillYearVO;
import com.imjcker.manager.charge.vo.ShowVO;
import com.imjcker.manager.charge.service.DatasourceBillService;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.DownloadUtils;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.elastic.model.SourceLogInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询客户账单（年-月-日）
 */
@RestController
@RequestMapping("/datasourceBill")
public class DatasourceBillController {
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径
    @Autowired
    private DatasourceBillService datasourceBillService;

    /**
     * 年账单（展示客户每个月的消费额、余额）
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listBillByYear")
    public CommonResult listBillByYear(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        DatasourceBillDayQuery query = jsonObject.toJavaObject(DatasourceBillDayQuery.class);
        DatasourceBillYearVO billYearPageInfo = datasourceBillService.listBillByYear(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, billYearPageInfo);
    }

    /**
     * 客户月账单（展示每天消费额、余额）
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listBillByMonth")
    public CommonResult listBillByMonth(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        DatasourceBillDayQuery query = jsonObject.toJavaObject(DatasourceBillDayQuery.class);
        DatasourceBillMonthVO billDayPageInfo = datasourceBillService.listBillByMonth(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, billDayPageInfo);
    }

    /**
     * 客户日账单（展示客户调用每个接口的计费方式、计费模式、调用量、单价、消费额、余额、余量以及详情list）
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listBillByDay")
    public CommonResult listBillByDay(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        DatasourceBillDayQuery query = jsonObject.toJavaObject(DatasourceBillDayQuery.class);
        ShowVO showVO = datasourceBillService.listBillByDay(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, showVO);
    }

    /**
     * 日账单详情接口
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listBillDetail")
    public CommonResult listBillDetail(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        DatasourceBillDayQuery query = jsonObject.toJavaObject(DatasourceBillDayQuery.class);
        DatasourceBillDetailVO datasourceBillDetailVO = datasourceBillService.listBillByDayDetail(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, datasourceBillDetailVO);
    }

    /**
     * 查询时间段类ES中的全量数据
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @PostMapping("/getQueryInfoListByTime")
    public CommonResult getQueryInfoListByTime(@RequestBody JSONObject jsonObject) throws ParseException {
        long startTime;
        long endTime;
        //参数为空，默认查询今天
        if (null == jsonObject || jsonObject.size()==0) {
            startTime = Long.parseLong(DateUtil.getBeforeTimestamp(0));//获取当天起始时间戳
            endTime = System.currentTimeMillis();//获取当前时间
        } else {
            startTime = jsonObject.getLong("startTime");
            endTime = jsonObject.getLong("endTime");
        }
        List<SourceLogInfo> list = datasourceBillService.getQueryInfoListByTime(startTime, endTime);
        Map<String, Object> result = new HashMap<>();
        result.put("size", list.size());
        result.put("list", list);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * @param sourceName 客户名称
     * @param startTime  起始时间
     * @param endTime    结束时间
     * @param response
     */
    @ApiOperation(value = "账单导出", notes = "账单导出")
    @GetMapping(value = "billExport")
    public void billExport(@RequestParam(name = "sourceName") String sourceName, @RequestParam(name = "startTime") String startTime,
                           @RequestParam(name = "endTime") String endTime, HttpServletResponse response) throws IOException {
        String filePath = FILE_PATH;
        String fileName = File.separator + "source-" +sourceName+ DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        datasourceBillService.downloadBill(sourceName, Long.valueOf(startTime), Long.valueOf(endTime), filePath, fileName);
        DownloadUtils.downloadFile(filePath + fileName + ".csv", fileName.substring(1) + ".csv", response);
    }

    /**
     * 模拟定时任务生成账单基础数据
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/saveTest")
    public CommonResult saveTest(@RequestBody JSONObject jsonObject) {
        Long startTime = jsonObject.getLong("startTime");
        Long endTime = jsonObject.getLong("endTime");
        datasourceBillService.saveDatasourceBillByDay(startTime, endTime);
        datasourceBillService.saveDatasourceBillByMonth(startTime, endTime);
        datasourceBillService.saveDatasourceBillByYear(startTime, endTime);
//        List<DatasourceChargeCount> result = datasourceBillService.test(startTime, endTime);
//        List<SourceLogInfo> result datasourceBillService.getQueryInfoListByTime(startTime, endTime);
        return CommonResult.success();
    }
    @PostMapping("/saveTest1")
    public CommonResult saveTest1(@RequestBody JSONObject jsonObject) {
        datasourceBillService.saveDatasourceBillByDay(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/saveTest2")
    public CommonResult saveTest2(@RequestBody JSONObject jsonObject) {
        datasourceBillService.saveDatasourceBillByMonth(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/saveTest3")
    public CommonResult saveTest3(@RequestBody JSONObject jsonObject) {
        datasourceBillService.saveDatasourceBillByYear(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
}
