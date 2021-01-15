package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.imjcker.api.common.util.OperatorCache;
import com.imjcker.api.common.util.ThirdResultContainDataUtil;
import com.imjcker.api.common.vo.*;
import com.imjcker.gateway.model.QueryInfo;
import com.imjcker.gateway.queue.ApiHandler;
import com.imjcker.gateway.service.AmqpSenderService;
import com.imjcker.gateway.service.KafkaService;
import com.imjcker.gateway.util.InvokeUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thh
 */
public class ResultFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultFilter.class);
    @Autowired
    private AmqpSenderService amqpSenderService;
    @Value("${esCount.ignoreFlag}")
    private String ignoreFlag;


    @Autowired
    private KafkaService kafkaService;


    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setCharacterEncoding("UTF-8");
        String responseBody = ctx.getResponseBody();
        LOGGER.debug("responseBody={}", responseBody);
        HttpServletRequest request = ctx.getRequest();
        InputStream stream = ctx.getResponseDataStream();
        MonitorMsgPoJo log = MonitorUtil.getLog();
        Long responseTime = System.currentTimeMillis();
        log.setResponseTime(responseTime);
        log.setSpendTime(responseTime - log.getRequestTime());
        // 标记可表示是否查询成功,如不成功则退款,更新redis余额
        boolean queryFlag;      // 查询标记
        boolean resultFlag;     // 查得标记
        String jsonConfig = (String) request.getAttribute("jsonConfig");
        String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
        Integer apiId = (Integer) request.getAttribute("apiId");
        String chargeResultStr = null;
        String version = (String) request.getAttribute(ZuulHeader.REQUEST_VERSION);
        CommonResult commonResult = new CommonResult();
        if (responseBody != null) {
            //内部服务调用,包装结果
            if (InvokeUtil.isInternalInvoke(request)) {
                Integer statusCode = JSONObject.parseObject(responseBody).getInteger("errorCode");
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("statusCode", statusCode == null ? 200 : statusCode);
                resultMap.put("resultString", responseBody);
                responseBody = JSONObject.toJSONString(resultMap);
            }
            ctx.addZuulResponseHeader("content-type", "application/json;charset=UTF-8");
            log.setResultMsg(responseBody);
            log.setErrorCode((Integer) ctx.getRequest().getAttribute("errorCode"));
            chargeResultStr = responseBody;
            ResponseBuilder responseBuilder = JSON.parseObject(responseBody, ResponseBuilder.class);
            commonResult.setCode(responseBuilder.getErrorCode());
            commonResult.setMessage(responseBuilder.getErrorMsg());
            commonResult.setData(responseBuilder.getUid());
            ctx.setResponseBody(responseBody);
        } else if (stream != null && ctx.getResponseStatusCode() != 500) {
            String charset = ctx.getResponse().getCharacterEncoding();
            try {
                String body = StreamUtils.copyToString(stream, Charset.forName(charset));
                Integer code = JSONObject.parseObject(body).getInteger("errorCode");
                if (code != null) {
                    log.setErrorCode(code);
                } else
                    log.setErrorCode(ctx.getResponseStatusCode());
                log.setResultMsg(body);
                //内部服务调用,包装结果
                if (InvokeUtil.isInternalInvoke(request)) {
                    Map<String, Object> resultMap = new HashMap<>();
                    // code == null ? 200 : code  解决未转码的正确响应
                    resultMap.put("statusCode", code == null ? 200 : code);
                    resultMap.put("resultString", body);
                    body = JSONObject.toJSONString(resultMap);
                    ctx.addZuulResponseHeader("content-type", "application/json;charset=UTF-8");
                }
                LOGGER.debug("ResultMsg : {}", JSONObject.toJSONString(log));
                chargeResultStr = body;
                String sign = "";
                if (Constant.REQUEST_VERSION_NOW.equals(version)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("body", JSON.parse(body));
                    //LOGGER.info("签名body={}",respStr);
                    commonResult.setData(jsonObject);
                    commonResult.setCode(ResultStatusEnum.SUCCESS.getCode());
                    commonResult.setMessage(ResultStatusEnum.SUCCESS.getMessage());
                }
                ctx.setResponseBody(body);
            } catch (Exception e1) {
                LOGGER.error("发生异常: {}", e1.getMessage());
                log.setErrorCode(9999);
                log.setResultMsg(e1.getMessage());
                LOGGER.error(LogPojo.getErrorLogMsg("返回结果出错", null, e1));
            }
        } else {
            String uid = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
            ResponseBuilder response = ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode())
                    .errorMsg(ResultStatusEnum.TFB_INTERNAL_ERROR.getMessage())
                    .build();
            log.setErrorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode());
            String json = JSON.toJSONString(response);
            //内部服务调用,包装结果
            if (InvokeUtil.isInternalInvoke(request)) {
                Integer statusCode = JSONObject.parseObject(json).getInteger("errorCode");
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("statusCode", statusCode == null ? 200 : statusCode);
                resultMap.put("resultString", json);
                json = JSONObject.toJSONString(resultMap);
                ctx.addZuulResponseHeader("content-type", "application/json;charset=UTF-8");
            }
            log.setResultMsg(json);
            LOGGER.debug("ResultFilter: {}", log);
            chargeResultStr = json;
            commonResult.setCode(ResultStatusEnum.SERVICE_ERROR.getCode());
            commonResult.setMessage(ResultStatusEnum.SERVICE_ERROR.getMessage());
            commonResult.setData(uid);
            ctx.setResponseBody(json);
        }
        if (Constant.REQUEST_VERSION_NOW.equals(version)) {
            ctx.setResponseBody(JSON.toJSONString(commonResult, SerializerFeature.WriteMapNullValue));
        }
        // 只有扣费标识为 YES 的时候才能退款,即扣费成功才会有退款
        boolean balanceFlag = "YES".equals(request.getAttribute("isCharge"));
        // 是否外部数据源
        boolean isDSFlag = StringUtils.isNotBlank(jsonConfig) && JSONObject.parseObject(jsonConfig).containsKey("type");
        boolean isCharge = true;
        if (isDSFlag && balanceFlag) {
            String type = JSON.parseObject(jsonConfig).getString("type");
            // 是否查询接口成功
            queryFlag = OperatorCache.resultIsToCache(chargeResultStr, type);
            // 查询接口都没成功,退款
            if (!queryFlag) {
                isCharge = false;
            } else {
                // 是否查得数据成功
                resultFlag = ThirdResultContainDataUtil.containData(chargeResultStr, type);
            }
        }
        double price = log.getPrice();
        String chargeUuid = log.getBillingRulesUuid();
        // 在扣款成功的标识上判断是否发送es扣款
        Boolean isChargeCopy = balanceFlag && isCharge;
        // 发送计费kafka到flink, 清除缓存不需要发送计费kafka和请求mq
        boolean isCleanCache = StringUtils.isNotBlank(request.getHeader(Constant.API_CACHE))
                && Constant.API_CACHE.equals(request.getHeader(Constant.API_CACHE));
        if (isDSFlag && StringUtils.isNotBlank(chargeResultStr) && !isCleanCache) {
            CustomerChargeMessageVo customerChargeMessageVo = CustomerChargeMessageVo.builder()
                    .messageMode(MessageModeEnum.CUSTOMER_CHARGE.getCode())
                    .appKey(appKey)
                    .apiId(apiId)
                    .type(JSONObject.parseObject(jsonConfig).getString("type"))
                    .result(chargeResultStr)
                    .build();
/*            new ApiHandler() {
                @Override
                public void handle() {
                    try {
                        String message = JSON.toJSONString(customerChargeMessageVo);
                        kafkaService.sendCustomerFlink(message);
                        LOGGER.error("发送计费kafka到flinkcustomerChargeMessageVo={}", JSON.toJSONString(customerChargeMessageVo));
                    } catch (Exception e) {
                        LOGGER.error("发送计费kafka到flink失败: {}", e.getMessage());
                    }
                }
            }.putQueue();*/
        }
        //是否通过了appkey的校验
        boolean isRightKey = "YES".equals(request.getAttribute("isRightKey"));
//        if (StringUtils.isBlank(request.getHeader(ignoreFlag)) && isRightKey && !isCleanCache) {
        if (StringUtils.isBlank(request.getHeader(ignoreFlag)) && !isCleanCache) {
            //异步发送请求信息到mq
            new ApiHandler() {
                public void handle() {
                    //ES存储下游请求信息，包装参数
                    QueryInfo info = QueryInfo.builder()
                            .appKey(log.getAppkey() == null ? "null" : log.getAppkey())
                            .uid(log.getRequestUuid() == null ? "null" : log.getRequestUuid())
                            .createTime(log.getRequestTime() == null ? 0 : log.getRequestTime())
                            .reqParam(log.getRequestParams() == null ? "null" : log.getRequestParams().toString())
                            .responseCode(log.getErrorCode())
                            .result(log.getResultMsg() == null ? "null" : log.getResultMsg().toString())
                            .spendTime(log.getSpendTime() == null ? 0 : log.getSpendTime())
                            .ipv4(log.getIpv4() == null ? "null" : log.getIpv4())
                            .url(log.getUrl() == null ? "null" : log.getUrl())
                            .apiId(log.getApiId() == null ? 0 : log.getApiId())
                            .apiName(log.getApiName() == null ? "null" : log.getApiName())
                            .chargeUuid(chargeUuid == null ? "null" : chargeUuid)
                            .price(price)
                            .chargeFlag(isChargeCopy)
                            .build();
                    try {
                        //发送MQ
                        String json = JSON.toJSONString(info);
                        amqpSenderService.convertAndSend(json);
                    } catch (Exception e) {
                        LOGGER.error("下游ES构造异步调用请求出错: ", e);
                    }
                }
            }.putQueue();
        }
        return null;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
}
