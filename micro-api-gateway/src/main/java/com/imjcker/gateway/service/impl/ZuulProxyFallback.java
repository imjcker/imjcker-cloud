package com.imjcker.gateway.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ZuulProxyFallback implements FallbackProvider{

    private static Logger logger = LoggerFactory.getLogger(ZuulProxyFallback.class);

    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {

        logger.info("请求服务超时，执行服务降级处理。");
        return fallbackResponse();
    }

    /**
     * 需要降级处理的serviceId，'*'代表所有服务,本类可配置为return "micro-gateway-client-service"
     * @return
     */
    @Override
    public String getRoute() {
        return "*";
    }

    /**
     * 降级处理逻辑
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse() {
        logger.warn("enter fallback method");
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {

                ResponseBuilder response = ResponseBuilder.builder()
                        .uid("")
                        .errorCode(ResultStatusEnum.TFB_INTERNAL_REFUSED.getCode())
                        .errorMsg(ResultStatusEnum.TFB_INTERNAL_REFUSED.getMessage())
                        .build();
                return new ByteArrayInputStream(JSONObject.toJSONString(response).getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
}
