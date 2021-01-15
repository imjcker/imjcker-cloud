package com.imjcker.api.handler.util;

import com.imjcker.api.common.http.proxy.OkHttp1;
import com.imjcker.api.common.http.proxy.SocketUtil;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.OperatorCache;
import com.imjcker.api.common.vo.Result;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.po.RequestParamAndValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class RequestUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 根据环境获取url，请求为socket时，path为空
     *
     * @param apiInfo
     * @return
     */
    public static String getUrlByEnvironment(ApiInfoVersions apiInfo, String environment) {
        Integer protocolType = apiInfo.getBackendProtocolType();
        String url = "";
        if (com.imjcker.api.common.vo.Constant.GATEWAY_CLIENT_SERVICE_B.equals(environment))
            url = isSocket(protocolType) ? apiInfo.getBackEndAddressB() : (apiInfo.getBackEndAddressB() + apiInfo.getBackEndPath());
        else
            url = isSocket(protocolType) ? apiInfo.getBackEndAddress() : (apiInfo.getBackEndAddress() + apiInfo.getBackEndPath());
        LOGGER.debug("environment:{},url:{}", environment, url);
        return url;
    }

    public static boolean isSocket(Integer protocolType) {
        return 3 == protocolType;
    }

    private static String getResultKey(String appkey, String md5, String uniqueUuid) {
        return StringUtils.join(Constant.REDIS_KEY_RESULT, uniqueUuid, ":", appkey, ":", md5);
    }

    /**
     * 设置异步收集字段，然后根据策略是否存入缓存
     *
     * @param result
     * @param async
     * @param appkey
     * @param md5
     * @param cacheTime
     * @param uniqueUuid
     * @param type
     * @param isCache
     */
    public static void putAsyncOrResultToCache(Integer apiId, String result, AsyncModel async, String appkey, String md5,
                                               Integer cacheTime, String uniqueUuid, String type, boolean isCache) {
        if (result != null && isCache && OperatorCache.resultIsToCache(result, type)) {
            async.setSourceStatus(1);
            async.setTargetStatus(1);
            async.setCached(1);
            RedisUtil.setResultToCache(apiId, appkey, md5, result, cacheTime, uniqueUuid, type);
        } else {
            async.setSourceStatus(2);
            async.setTargetStatus(2);
            async.setCached(2);
        }
    }

    /**
     * 2019-05-28
     * 获取请求协议
     *
     * @param protocol
     * @return
     */
    public static String takeProtocol(Integer protocol) {
        if (1 == protocol) {
            return ConstantEnum.ProtocolType.http.getName();
        } else if (2 == protocol) {
            return ConstantEnum.ProtocolType.https.getName();
        } else if (isSocket(protocol)) {
            return ConstantEnum.ProtocolType.socket.getName();
        } else {
            return null;
        }
    }

    /**
     * 根据是否缓存获取结果,是否转换成json
     *
     * @param appKey
     * @param uniqueUuid
     * @param cacheTime
     * @param md5
     * @param async
     * @return
     */
    public static String pullResultFromCacheJsonNoHtml(Integer apiId, String appKey, String uniqueUuid, Integer cacheTime, String md5, AsyncModel async) {
        String result;
        if (cacheTime != -1) {
            result = RedisUtil.getResultFromCache(apiId, appKey, md5, uniqueUuid);
            if (StringUtils.isNotBlank(result)) {
                asyncSetAndPut(async);
                return result;
            }
        }
        return null;
    }

    /**
     * 2019-05-28
     * 更新异步字段
     *
     * @param async
     */
    private static void asyncSetAndPut(AsyncModel async) {
        if (async == null)
            return;
        async.setCached(1);
        async.setSourceStatus(2);
        async.setTargetStatus(1);
        AsyncCollectionUtil.putAsyncModel(async);
    }

    /**
     * 去除map中的空值
     *
     * @param params
     * @return
     */
    public static Map<String, String> deleteBlankValue4Map(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            return params.entrySet().stream()
                    .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                    .collect(toMap(entry -> (String) entry.getKey(), entry -> (String) entry.getValue()));
        }
        return params;
    }

    private static String[] splitHostAndPort(String url) {
        return url.split(":");
    }

    /**
     * 2019-05-28
     * 发送请求method
     *
     * @param protocol 网络协议 http socket等
     * @return result
     */
    public static Result sendRequest1(String method, String url, Map<String, String> bodyVariables,
                                      Map<String, String> queryVariables, Map<String, String> headerVariables,
                                      String json, String xml, Long timeout, String protocol,
                                      String retryFlag, int retryCount, boolean isIgnoreVerify) {
        Result result = null;
        if (ConstantEnum.HttpMethod.get.getName().equals(method)) {
            result = OkHttp1.get(url, queryVariables, headerVariables, timeout,
                    protocol, retryFlag, retryCount, isIgnoreVerify);
        } else if (ConstantEnum.HttpMethod.post.getName().equals(method)) {
            if (json != null) {
                result = OkHttp1.postJson(url, queryVariables, json,
                        headerVariables, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
            } else if (xml != null) {
                result = OkHttp1.postXml(url, queryVariables, xml,
                        headerVariables, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
            } else {
                result = OkHttp1.postKV(url, queryVariables, bodyVariables,
                        headerVariables, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
            }
        } else if (RequestUtil.isSocket(ConstantEnum.ProtocolType.getValueByName(protocol))) {
            String[] arr = splitHostAndPort(url);
            result = SocketUtil.send2TcpServer(xml, arr[0], Integer.parseInt(arr[1]), timeout.intValue());
        }
        return result;
    }

    public static List<RequestParamAndValue> buildParams(Map<String, String> params) {
        List<RequestParamAndValue> paramAndValues = new ArrayList<>();
        for (Map.Entry entry : params.entrySet()) {
            RequestParamAndValue value = new RequestParamAndValue();
            value.setParamName(entry.getKey().toString());
            value.setParamValue(entry.getValue().toString());
            paramAndValues.add(value);
        }
        return paramAndValues;
    }

    /**
     * @param request
     * @Description : 获取前端参数
     * @Return : java.util.Map<java.lang.String,java.lang.Object>
     * @Date : 2019/9/23 14:58
     */
    public static Map<String, Object> getRequestParams(HttpServletRequest request) {
        // 获取请求参数
        Map<String, Object> requestParams = new HashMap<>();
        // 获取请求体参数
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            requestParams.put(paraName, request.getParameter(paraName));
        }
        // 获取header参数
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if (Operator.headerOper(key)) {
                requestParams.put(key, request.getHeader(key));
            }
        }
        return requestParams;
    }


    /**
     * 判断接口请求是否转码
     *
     * @return
     */
    public static Boolean isTransCode(String inmgrCode, String configJson) {
        LOGGER.debug("请求传入转码标识={},configJson是否为空:{}", inmgrCode, StringUtils.isBlank(configJson));
        Boolean isTransCode;
        //只有transCode=0且configJson不等于空才具备转码条件，默认不传transCode时值为0.
        if (Constant.PARAM_TRANS_CODE.equals(inmgrCode) && StringUtils.isNotBlank(configJson)) {//新接口
            isTransCode = true;
        } else {//老接口header不含transCode，按照configJson判断转码
            isTransCode = false;
        }
        LOGGER.debug("是否转码：{}", isTransCode);
        return isTransCode;
    }

    public static String sendRequest(String method, String url, Map<String, String> bodyVariables,
                                     Map<String, String> queryVariables, Map<String, String> headerVariables,
                                     String json, String xml, Long timeout, String protocol,
                                     String retryFlag, int retryCount) {
        return null;
    }
}
