package com.imjcker.api.handler.strategy;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.util.ConstantEnum;
import com.imjcker.api.handler.util.ExtractUtil;
import com.imjcker.api.handler.util.RequestUtil;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.po.ApiInfoVersionsWithBLOBs;
import com.imjcker.api.handler.util.ConstantEnum;
import com.imjcker.api.handler.util.ExtractUtil;
import com.imjcker.api.handler.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author WT
 * @Date 14:16 2019/8/21
 * @Version CommonStrategy v1.0
 * @Desicrption
 */
@Service("CommonStrategy")
public class CommonStrategy implements ApiStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CommonStrategy.class);

    @Override
    public String handleParams(MainEntity.Params params, MainEntity mainEntity, Map<String, Object> jsonConfig, String url) throws IOException, NoSuchAlgorithmException {
        ApiInfoVersionsWithBLOBs api = mainEntity.getApiInfo();
        Integer protocolType = api.getBackendProtocolType();
        String protocol = RequestUtil.takeProtocol(protocolType);
        String uid = jsonConfig.getOrDefault("uid", "").toString();
        Long timeout = Long.parseLong(api.getBackEndTimeout().toString());
        String method = null;
        if (1 == protocolType || 2 == protocolType) {
            method = api.getBackEndHttpMethod() == 1 ? ConstantEnum.HttpMethod.get.getName() : ConstantEnum.HttpMethod.post.getName();
        }
        String retryFlag = jsonConfig.getOrDefault("retry_flag", "false").toString();
        Integer retryCount = Integer.valueOf(jsonConfig.getOrDefault("retry_count", 0).toString());
        return buildJson(params, url, method, uid, timeout, protocol, retryFlag, retryCount, OKHTTP, false, api.getApiId(), api.getApiName()).toJSONString();
    }

    @Override
    public ApiStrategyMapping getStrategy() {
        return ApiStrategyMapping.COMMON;
    }

    @Override
    public String doRequest(ApiInfoVersions api, MainEntity.Params params, String retryFlag,
                            int retryCount, Map<String, Object> map, String environment) {

        String url = RequestUtil.getUrlByEnvironment(api, environment);
        Integer protocolType = api.getBackendProtocolType();
        String protocol = RequestUtil.takeProtocol(protocolType);
        //上游接口唯一标识
        String uniqueUuid = api.getUniqueUuid();
        // method
        String method = null;
        if (1 == protocolType || 2 == protocolType) {
            method = api.getBackEndHttpMethod() == 1 ? ConstantEnum.HttpMethod.get.getName() : ConstantEnum.HttpMethod.post.getName();
        }
        logger.info("请求信息:\r\nuniqueUuid: {} \r\nProtocol: {} url: {}\r\nMethod: {}\r\nHeaders: {}\r\nQuerys: {}\r\nBodys: {}\r\nJSON: {}\r\nXML: {}",
                uniqueUuid,
                protocol,
                url,
                method,
                JSONObject.toJSONString(params.getHeaderVariables()),
                JSONObject.toJSONString(params.getQueryVariables()),
                JSONObject.toJSONString(params.getBodyVariables()),
                params.getJson(),
                params.getXml());
        // timeout
        Long timeout = Long.parseLong(api.getBackEndTimeout().toString());
        String result = RequestUtil.sendRequest(method, url, params.getBodyVariables(), params.getQueryVariables(),
                params.getHeaderVariables(), params.getJson(), params.getXml(),
                timeout, protocol, retryFlag, retryCount);
        logger.info("第三方原始报文: {}", result);
        result = ExtractUtil.xml2Json(result).toString();
        return result;
    }
}
