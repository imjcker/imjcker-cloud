package com.imjcker.manager.charge.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.CompanyBillDayQuery;
import com.imjcker.manager.charge.vo.ShowVO;
import com.imjcker.manager.charge.service.CustomerBillService;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillDetail;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillMonth;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillYear;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.DownloadUtils;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.elastic.model.QueryInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
@RequestMapping("/bill")
public class CustomerBillController {
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径
    @Autowired
    private CustomerBillService customerBillService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String selfServiceName;

    /**
     * 客户年账单（展示客户每个月的消费额、余额）
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/listBillByYear")
    public CommonResult listBillByYear(@RequestBody JSONObject jsonObject) {
        if (jsonObject.isEmpty())
            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
        CompanyBillDayQuery query = jsonObject.toJavaObject(CompanyBillDayQuery.class);
        ShowVOCompanyBillYear billYearPageInfo = customerBillService.listBillByYear(query);
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
        CompanyBillDayQuery query = jsonObject.toJavaObject(CompanyBillDayQuery.class);
        ShowVOCompanyBillMonth billDayPageInfo = customerBillService.listBillByMonth(query);
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
        CompanyBillDayQuery query = jsonObject.toJavaObject(CompanyBillDayQuery.class);
        ShowVO showVO = customerBillService.listBillByDay(query);
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
        CompanyBillDayQuery query = jsonObject.toJavaObject(CompanyBillDayQuery.class);
        ShowVOCompanyBillDetail billDayPageInfo = customerBillService.listBillByDayDatail(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, billDayPageInfo);
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
        if (null == jsonObject || jsonObject.size() == 0) {
            startTime = Long.parseLong(DateUtil.getBeforeTimestamp(0));//获取当天起始时间戳
            endTime = System.currentTimeMillis();//获取当前时间
        } else {
            startTime = jsonObject.getLong("startTime");
            endTime = jsonObject.getLong("endTime");
        }
        List<QueryInfo> list = customerBillService.getQueryInfoListByTime(startTime, endTime);
        Map<String, Object> result = new HashMap<>();
        result.put("size", list.size());
        result.put("list", list);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }


    /**
     * 获取服务器-业务IP地址
     *
     * @return
     */
    @PostMapping("/getServerURL")
    public CommonResult getServerURL() {
        List<ServiceInstance> instances = discoveryClient.getInstances(selfServiceName);
        String url = ((EurekaDiscoveryClient.EurekaServiceInstance) instances.get(0)).getInstanceInfo().getHomePageUrl();
        if (url.contains("172.")){//测试环境172网段
            return new CommonResult(ResultStatusEnum.SUCCESS, url.substring(0, url.length() - 1));
        }else{//生产环境26网段、亿字节126网段
            url= getClientServerURL();
            return new CommonResult(ResultStatusEnum.SUCCESS, url);
        }
    }

    /**
     * 获取服务器IP地址-99网段
     *
     * @return
     */
    @PostMapping("/getClientServerURL")
    public String getClientServerURL() {
        String ip = null;
        String ifconfigResult = null;
        String[] cmd = new String[]{"/bin/sh", "-c", "ifconfig"};
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            ifconfigResult = sb.toString();
            if (StringUtils.isNotBlank(ifconfigResult)){
                int index = ifconfigResult.indexOf("inet 99");
                ip = "http://"+ifconfigResult.substring(index+5,index+16)+":6080".replaceAll(" ","");
            }
        } catch (IOException e) {
        }
        return ip;
    }

    /**
     * @param appKey    客户名称
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param response
     */
    @ApiOperation(value = "账单导出", notes = "账单导出")
    @GetMapping(value = "billExport")
    public void billExport(@RequestParam(name = "appKey") String appKey, @RequestParam(name = "startTime") String startTime,
                           @RequestParam(name = "endTime") String endTime, HttpServletResponse response) throws IOException {
        String filePath = FILE_PATH;
        String fileName = File.separator + "appKey-" + appKey + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        customerBillService.downloadBill(appKey, Long.valueOf(startTime), Long.valueOf(endTime), filePath, fileName);
        DownloadUtils.downloadFile(filePath + fileName + ".csv", fileName.substring(1) + ".csv", response);
    }

    /**
     * 模拟定时任务生成账单基础数据
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/saveTest1")
    public CommonResult saveTest1(@RequestBody JSONObject jsonObject) {
        customerBillService.saveCustomerChargeByDay(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/saveTest2")
    public CommonResult saveTest2(@RequestBody JSONObject jsonObject) {
        customerBillService.saveCustomerChargeByMonth(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
    @PostMapping("/saveTest3")
    public CommonResult saveTest3(@RequestBody JSONObject jsonObject) {
        customerBillService.saveCustomerChargeByYear(null,null);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
}
