package com.imjcker.api.handler.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.imjcker.api.common.exception.vo.BusinessException;
import com.imjcker.api.common.http.util.NetWorkUtils;
import com.imjcker.api.common.model.SourceLogInfo;
import com.imjcker.api.common.util.BeanCustomUtils;
import com.imjcker.api.common.util.OperatorCache;
import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.api.common.util.ThirdResultContainDataUtil;
import com.imjcker.api.common.vo.*;
import com.imjcker.api.handler.constant.ReqType;
import com.imjcker.api.handler.model.ExitParamModel;
import com.imjcker.api.handler.model.RespBillingRules;
import com.imjcker.api.handler.plugin.queue.ApiHandler;
import com.imjcker.api.handler.po.RespDatasourceCharge;
import com.imjcker.api.handler.service.AmqpSenderService;
import com.imjcker.api.handler.service.ExitService;
import com.imjcker.api.handler.service.KafkaService;
import com.imjcker.api.handler.service.MicroChargeService;

import com.imjcker.api.handler.util.ExtractUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


@Controller
@RequestMapping("/exit")
public class ExitController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExitController.class);
    public static final String ENCODING = "UTF-8";
    private final AmqpSenderService amqpSenderService;

    @Autowired
    private ExitService exitService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private MicroChargeService microChargeService;

    @Autowired
    private KafkaService kafkaService;

    @Value("${kafka.exitTopicName}")
    private String interfaceTopcName;

    @Autowired
    public ExitController(AmqpSenderService amqpSenderService) {
        this.amqpSenderService = amqpSenderService;
    }

    /**
     * OKHttp通用接口
     */
    @PostMapping(value = "/general", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public Object commonRequest(@RequestBody JSONObject jsonObject) throws IOException {
        String apiHealth = request.getHeader("API_HEALTH");
        if (StringUtils.isNotBlank(apiHealth)) {
            LOGGER.error("API_HEALTH");
            return ResponseBuilder.builder()
                    .uid(null)
                    .errorCode(ResultStatusEnum.TFB_SUCCESS.getCode())
                    .errorMsg(ResultStatusEnum.TFB_SUCCESS.getMessage())
                    .build();
        }
        LOGGER.debug(NetWorkUtils.getIPAddress(request));
        if (null == jsonObject) {
            LOGGER.error("参数为空");
            return JSON.toJSONString(new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL));
        }
        ExitParamModel model = jsonObject.toJavaObject(ExitParamModel.class);
        String uid = model.getUid();//流水号
        String url = model.getUrl();//请求地址
        String method = model.getMethod();//请求方法
        Long timeout = model.getTimeout();//超时
        String protocol = model.getProtocol();//协议
        String retryFlag = model.getRetryFlag();//重试标识
        int retryCount = model.getRetryCount();//重试次数
        boolean isIgnoreVerify = model.isIgnoreVerify();//true表示https忽略证书验证
        String apiName = model.getApiName();
        Integer apiId = model.getApiId();
        String sourceName = model.getSourceName();
        ExitParamModel.Data data = model.getData();//请求数据
        if (null == data) {
            LOGGER.error("data参数为空");
            return JSON.toJSONString(new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL));
        }
        Map<String, String> headerVariables = data.getHeaderList();
        Map<String, String> queryVariables = data.getQueryList();
        Map<String, String> bodyVariables = data.getBodyList();
        String json = data.getJson();
        String xml = data.getXml();
        String type = model.getType();
        LOGGER.info("请求信息:\r\nuid: {} \r\napiId: {} \r\napiName: {}\r\nProtocol: {} url: {}\r\nMethod: {}\r\n" +
                        "Headers: {}\r\nQuerys: {}\r\nBodys: {}\r\n" +
                        "JSON: {}\r\nXML: {}\r\ntimeOut: {}\r\nretryFlag: {}\r\nretryCount: {}\r\nisIgnoreVerify:{}\r\n" +
                        "sourceName:{}\r\ntype:{}\r\n",
                uid, apiId, apiName, protocol, url, method,
                JSONObject.toJSONString(headerVariables),
                JSONObject.toJSONString(queryVariables),
                JSONObject.toJSONString(bodyVariables),
                json, xml, timeout, retryFlag, retryCount, isIgnoreVerify, sourceName, type);
        Result result = new Result();
        String params = (null == json) ? xml : json;
        long startTime = System.currentTimeMillis();
        switch (type) {
            case ReqType.OKHTTP:
                result = exitService.commonOkHttpRequest(method, url, bodyVariables, queryVariables, headerVariables, json, xml, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
                break;
            case ReqType.HTTP_CLIENT:
                result = exitService.commonHttpClientRequest(url, method, headerVariables, params, bodyVariables, ENCODING, timeout, isIgnoreVerify);
                break;
            case ReqType.SOCKET:
                result = exitService.socketRequest(method, url, bodyVariables, queryVariables, headerVariables, json, xml, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
            default:
                break;
        }
        long endTime = System.currentTimeMillis();
        Result resultCopy = result;
        if (StringUtils.isNotBlank(sourceName) && sourceName.startsWith("E")) {
            new ApiHandler() {
                public void handle() {
                    //根据apiId查询计费规则和单价信息
                    JSONObject object = new JSONObject();
                    object.put("apiId", apiId);
                    String chargeUuid;
                    JSONObject charge = RedisUtil.get("datasource_charge:" + apiId);
                    if (null == charge) {
                        String chargeStr = microChargeService.getChargeByApiId(object);
                        charge = JSONObject.parseObject(chargeStr);
                        chargeUuid = charge.getString("chargeUuid");
                    } else {
                        LOGGER.debug("datasource_charge:" + apiId + ":{}","datasource_charge:" + apiId);
                        chargeUuid = charge.getString("billingRulesUuid");
                    }
                    RespDatasourceCharge respDatasourceCharge = new RespDatasourceCharge();
                    respDatasourceCharge.setBillingRulesUuid(chargeUuid);
                    respDatasourceCharge.setPrice(charge.getBigDecimal("price"));
                    if (respDatasourceCharge == null) {
                        throw new BusinessException("数据源计费有误");
                    }
                    RespBillingRules respBillingRules = new RespBillingRules();
                    JSONObject jsonObject = RedisUtil.get("chargeRule:" + chargeUuid);
                    if (jsonObject == null) {
                        respBillingRules = microChargeService.selectByUuid(chargeUuid).getData();
                    } else {
                        LOGGER.debug("chargeRule:" + chargeUuid + ":{}", "chargeRule:" + chargeUuid);
                        respBillingRules = jsonObject.toJavaObject(RespBillingRules.class);
                    }
                    //判断是否计费字段
                    Boolean chargeFlag = getChargeFlag(respBillingRules, sourceName, resultCopy);
                    if (chargeUuid == null || StringUtils.isBlank(sourceName) || null == apiId) {
                        chargeFlag = false;
                    }
                    SourceLogInfo info = SourceLogInfo.builder()
                            .uid(uid)
                            .apiId(apiId == null ? 0 : apiId)
                            .apiName(apiName == null ? "null" : apiName)
                            .sourceUrl(url == null ? "null" : url)
                            .sourceName(sourceName == null ? "null" : sourceName)
                            .sourceReqParam(JSON.toJSONString(data) == null ? "null" : JSON.toJSONString(data))
                            .chargeUuid(chargeUuid == null ? "null" : charge.getString("billingRulesUuid"))
                            .price(charge.getDouble("price") == null ? 0 : charge.getDouble("price"))
                            .chargeFlag(chargeFlag)
                            .build();
                    BeanCustomUtils.copyPropertiesIgnoreNull(resultCopy.getSourceLogInfo(), info);
                    InterfaceLogInfo interfaceLogInfo = InterfaceLogInfo.builder()
                            .apiId(apiId)
                            .billingCycle(respBillingRules.getBillingCycle())
                            .billingType(respBillingRules.getBillingType())
                            .billingCycleLimit(respBillingRules.getBillingCycleLimit())
                            .chargeFlag(chargeFlag == null ? false : chargeFlag)
                            .build();
                    try {
                        //发送MQ
                        String json = JSON.toJSONString(info);
                        amqpSenderService.convertAndSend(json);
                        LOGGER.debug("构造异步调用请求成功:{} ", json);
                    } catch (Exception e) {
                        LOGGER.error("构造异步调用请求出错:{} ", e);
                    }
                    try {
                        //发送kafka
                        String json = JSON.toJSONString(interfaceLogInfo);
                        kafkaService.send(interfaceTopcName, json);
                        LOGGER.debug("fink-构造异步调用请求成功:{} ", json);
                    } catch (Exception e) {
                        LOGGER.error("fink-构造异步调用请求出错:{} ", e);
                    }
                }
            }.

                    putQueue();
        }
        String strResult = result.getResult();
        LOGGER.debug("调用{}, {}耗时：{} ms", apiId, apiName, endTime - startTime);
        // 结果为空
        if (StringUtils.isBlank(strResult)) {
            return JSON.toJSONString(CommonResult.fail(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION));
        } else {
            try {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("原始结果: {}", strResult);
                JSONObject object = ExtractUtil.xml2Json(strResult);
                if (("500").equals(object.getString("inmgrErrorCode"))) {
                    LOGGER.error("调用API{} ：{} 请求错误, 耗时：{} ms, 实际配置为：{}", apiId, apiName, endTime - startTime, timeout);
                    return JSON.toJSONString(new CommonResult(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION, ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getMessage()));
                }
            } catch (Exception e) {
                LOGGER.error("xml to json error");
                return JSON.toJSONString(CommonResult.ex(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getCode(), "Parse result to json error", ""));
            }
        }
        return JSON.toJSONString(CommonResult.success(strResult));
    }

    /**
     * @Description : 判断是否计费
     * @Date : 2020/4/2 10:18
     * @Auth : qiuwen
     */
    private Boolean getChargeFlag(RespBillingRules respBillingRules, String sourceName, Result result) {
//        RespBillingRules respBillingRules = microChargeService.selectByUuid(charge.getString("chargeUuid")).getData();
//        if(respBillingRules==null){
//            throw new BusinessException("计费规则uuid有误");
//        }
        //计费模式(默认1查询计费，2查得计费)
        Integer billingMode = respBillingRules.getBillingMode();
        //是否查询接口成功
        Boolean operatorBoolean = OperatorCache.resultIsToCache(result.getResult(), sourceName);
        //是否查的数据成功
        Boolean resultBoolean = ThirdResultContainDataUtil.containData(result.getResult(), sourceName);
        if (billingMode == 1 && operatorBoolean) {
            return true;
        } else if (billingMode == 2 && operatorBoolean && resultBoolean) {
            return true;
        } else {
            return false;
        }
    }
}
