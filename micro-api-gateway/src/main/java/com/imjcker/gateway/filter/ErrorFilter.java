package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.api.common.vo.*;
import com.imjcker.gateway.model.QueryInfo;
import com.imjcker.gateway.queue.ApiHandler;
import com.imjcker.gateway.service.AmqpSenderService;
import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ErrorFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(ErrorFilter.class);
    @Autowired
    private AmqpSenderService amqpSenderService;
    @Value("${error.path:/error}")
    private String errorPath;
    @Value("${esCount.ignoreFlag}")
    private String ignoreFlag;

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object run() {
        try {
            MonitorMsgPoJo log = MonitorUtil.getLog();
            RequestContext ctx = RequestContext.getCurrentContext();
            Throwable throwable = ctx.getThrowable();
            ErrorResponse error = new ErrorResponse();
            logger.info("进入ErrorFilter: {}", throwable);
            if ("health".equals(ctx.getResponseBody()) || "clean_agency_account".equals(ctx.getResponseBody())) {
                error.setErrorCode(200);
                error.setErrorMsg("success");
            } else {
                Map<String, String> headers = (Map<String, String>) ctx.get("zuulRequestHeaders");
                error.setUid(headers.get("zuul_requestuuid"));
                error.setErrorCode(500);
                if (throwable.getCause() != null) {
                    if (throwable.getCause().getCause() != null) {
                        Throwable clientThrowable = throwable.getCause().getCause().getCause();
                        if (clientThrowable != null && clientThrowable instanceof ClientException) {
                            String errorMsg = clientThrowable.getCause().getMessage();
                            error.setErrorMsg(errorMsg);
                        }
                    }
                }
            }
            HttpServletResponse response = ctx.getResponse();
            logger.info("ErrorFilter返回error为: {}", error);
            response.setContentType("application/json;charset=utf8");
            //页面点击清除缓存不计数
            double price = log.getPrice();
            boolean chargeFlag = false;
            String chargeUuid = log.getBillingRulesUuid();
            if (StringUtils.isBlank(chargeUuid))
                chargeUuid = "null";
            String chargeUuidCopy = chargeUuid;
            //是否通过了appkey的校验
            boolean isRightKey = "YES".equals(ctx.getRequest().getAttribute("isRightKey"));
            boolean isCleanCache = StringUtils.isNotBlank(ctx.getRequest().getHeader(Constant.API_CACHE))
                    && Constant.API_CACHE.equals(ctx.getRequest().getHeader(Constant.API_CACHE));
            if (StringUtils.isBlank(ctx.getRequest().getHeader(ignoreFlag)) && isRightKey && !isCleanCache) {
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
                                .chargeUuid(chargeUuidCopy == null ? "null" : chargeUuidCopy)
                                .price(price)
                                .chargeFlag(chargeFlag)
                                .build();
                        try {
                            //发送MQ
                            String json = JSON.toJSONString(info);
                            amqpSenderService.convertAndSend(json);
                        } catch (Exception e) {
                            logger.error("下游ES构造异步调用请求出错: ", e);
                        }
                    }
                }.putQueue();
            }

            PrintWriter writer = null;
            HttpServletRequest request = ctx.getRequest();
            String version = (String) request.getAttribute(ZuulHeader.REQUEST_VERSION);
            try {
                writer = response.getWriter();
                if (Constant.REQUEST_VERSION_NOW.equals(version)) {
                    CommonResult result = new CommonResult();
                    result.setCode(ResultStatusEnum.SERVICE_ERROR.getCode());
                    result.setMessage(ResultStatusEnum.SERVICE_ERROR.getMessage());
                    result.setData(error.getUid());
                    writer.println(JSONObject.toJSONString(result));
                } else {
                    writer.println(objectMapper.writeValueAsString(error));
                }
            } catch (IOException e) {
                logger.error("ErrorFilter : {}", e.getMessage());
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    private static class ErrorResponse {

        private String uid;

        private int errorCode;

        private String errorMsg;

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }


    }
}
