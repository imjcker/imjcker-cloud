package com.imjcker.api.handler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.api.common.http.proxy.OkHttp;
import com.imjcker.api.common.util.MD5Utils;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.OperatorCache;
import com.imjcker.api.common.vo.LogPojo;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.ChildEntity;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.*;
import com.imjcker.api.handler.service.RedisService;
import com.imjcker.api.handler.util.*;
import com.imjcker.api.handler.mapper.ApiRateDistributeMapper;
import com.imjcker.api.handler.service.ChildrenService;
import com.imjcker.api.handler.service.ClientService;
import com.imjcker.api.handler.service.GatewayClientService;
import com.imjcker.api.handler.service.MainService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class ClientServiceImpl implements ClientService {

    private static Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ApiRateDistributeMapper apiRateDistributeMapper;

    @Autowired
    private ChildrenService childrenService;

    @Autowired
    private MainService mainService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GatewayClientService gatewayClientService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getHost(Integer apiId, String uniqueUuid, Integer weight) throws IOException {

        List<ApiRateDistribute> list;
        String result = redisService.get(getRateDistribute(apiId), String.class);
        if (StringUtils.isNotBlank(result)) {
            list = objectMapper.readValue(result, new TypeReference<List<ApiRateDistribute>>(){});
        } else {
            ApiRateDistributeExample example = new ApiRateDistributeExample();
            ApiRateDistributeExample.Criteria criteria = example.createCriteria();
            criteria.andApiIdEqualTo(apiId);
            criteria.andStatusEqualTo(1);
            criteria.andWeightNotEqualTo(0);
            list = apiRateDistributeMapper.selectByExample(example);
        }
        if (list != null && list.size() != 0) {
            redisService.set(getRateDistribute(apiId), objectMapper.writeValueAsString(list));
            HashMap<String, Integer> map = new HashMap<>();
            map.put(uniqueUuid, weight);// 添加主接口的
            for (ApiRateDistribute apiRateDistribute : list) {
                map.put(apiRateDistribute.getUniqueUuid(), apiRateDistribute.getWeight());
            }
            String host = LoadBalance.getServer(map);
            // 1,：swdsfdfdf表示主接口 2,swdsfdfdf：表示子接口
            if (!uniqueUuid.equals(host)) {
                return StringUtils.join("2,", host);
            }
        }
        if (weight == 0) return null;
        return StringUtils.join("1,", uniqueUuid);
    }

    @Override
    public MainEntity getMainEntity(String versionId) {
        // 加缓存
        MainEntity mainEntity = getFromCache(versionId);
        if (mainEntity == null) {
            mainEntity = new MainEntity();
            mainEntity.setApiInfo(mainService.selectApiInfo(versionId));
            mainEntity.setBackParams(mainService.selectBackParams(versionId));
            mainEntity.setRequestParams(mainService.selectRequestParams(versionId));

            setToCache(versionId, mainEntity);
        }
        return mainEntity;
    }

    private void setToCache(String versionId, MainEntity apiInfo) {
        String key = getKey(versionId);
        try {
            redisService.set(key, apiInfo);
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("存入Redis出错", apiInfo, e));
        }
    }

    private MainEntity getFromCache(String versionId) {
        String key = getKey(versionId);
        try {
            return redisService.get(key, MainEntity.class);
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("从Redis取值出错", key, e));
        }
        return null;
    }

    private String getKey(String key) {
        return Constant.REDIS_KEY_PRE + "versionid:" + key;
    }

    private String getRateDistribute(Integer apiId) {
        return "api:ratedistribute:" + apiId;
    }

    private String getResultKey(Integer apiId,String appkey, String md5, String uniqueUuid) {
        return StringUtils.join(Constant.REDIS_KEY_RESULT, apiId, ":", appkey, ":", md5);
    }

    private void setResultToCache(Integer apiId,String appkey, String md5, String result, Integer timeout, String uniqueUuid, String type) {
        try {
            if (OperatorCache.resultIsToCache(result, type)) {
                setToCache(apiId,appkey, md5, result, timeout, uniqueUuid);
            }
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("存入Redis出错", result, e));
        }
    }

    private void setToCache(Integer apiId,String appkey, String md5, String result, Integer timeout, String uniqueUuid) {
        String key = getResultKey(apiId,appkey, md5, uniqueUuid);
        LOGGER.debug("uid-{}-存入redis缓存，key：{}，数据：{}", AsyncCollectionUtil.getUid(), key, result);
        redisService.set(key, result, timeout);
    }

    private String getResultFromCache(Integer apiId,String appkey, String md5, String uniqueUuid) {
        String key = getResultKey(apiId,appkey, md5, uniqueUuid);
        try {
            String result = redisService.get(key, String.class);
            LOGGER.debug("读取redis缓存,key: {}, 数据: {}", key, result);
            return result;
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("读取Redis出错", key, e));
        }
        return null;
    }

    @Override
    public MainEntity.Params handleMainEntity(MainEntity mainEntity, HttpServletRequest request, Map<String,Object> accountMap, JSONObject paramJson) {
        Map<String, String> headerVariables = new HashMap<String, String>(); // header中的参数
        Map<String, String> queryVariables = new LinkedHashMap<String, String>(); // query的参数,get方式有
        Map<String, String> bodyVariables = new LinkedHashMap<String, String>(); // body中的参数,post方式有
        List<BackendRequestParamsVersions> backParams = mainEntity.getBackParams();
        return (MainEntity.Params) convertParamsAbs(backParams,request, headerVariables, queryVariables,
                bodyVariables, mainEntity.getRequestParams(),true, accountMap,paramJson);
    }

    /**
     * 2019-05-27
     * 对父接口，子接口的请求参数转换合并到一个方法
     * @param backParams
     * @param headerVariables
     * @param queryVariables
     * @param bodyVariables
     * @param requestParams
     */
    private Object convertParamsAbs(List<?> backParams, HttpServletRequest request,
                                    Map<String, String> headerVariables, Map<String, String> queryVariables,
                                    Map<String, String> bodyVariables, List<RequestParamsVersions> requestParams,
                                    boolean isFather, Map<String,Object> accountMap, JSONObject paramJson) {
        String json = null; // json参数
        String xml = null; // xml参数
        Integer requestParamType = 0; // 请求参数类型：0：默认 1：json 2：xml
        String encodeParam = null; // 常量加密字段
        String encodeValue = null; // 常量加密字段值
        Integer encodeLocation = null; // 常量加密字段位置
        GrandEntityBackParam entity = null;
        for (Object object : backParams) {
            entity = (GrandEntityBackParam) object;
            if (ConstantEnum.ParamsType.json.getValue() == entity.getParamsType()) {
                requestParamType = 1;
                json = entity.getParamValue();
                break;
            } else if (ConstantEnum.ParamsType.xml.getValue() == entity.getParamsType()) {
                requestParamType = 2;
                xml = entity.getParamValue();
                break;
            }
            if (entity.getParamsType() == ConstantEnum.ParamsType.constant.getValue() // 暂存加密字段内容
                    && entity.getParamValue() != null && entity.getParamValue().split("[,，]").length > 1) {
                encodeLocation = entity.getParamsLocation();
                encodeParam = entity.getParamName();
                encodeValue = entity.getParamValue();
                break;
            }
        }
        if (requestParamType == 1) {// json情况
            for (Object object : backParams) {
                entity = (GrandEntityBackParam) object;
                if (entity.getParamsType() != ConstantEnum.ParamsType.json.getValue()) {// 除去json字段，非json字段才转换
                    json = Operator.replace(json, entity.getParamName(),
                            getValueXMLandJson(request, entity, requestParams,paramJson));
                }
            }
            if (isFather) {
                if (json != null) json = replaceDateExpression(json);
            }
        } else if (requestParamType == 2) {// xml情况
            for (Object object : backParams) {
                entity = (GrandEntityBackParam) object;
                if (entity.getParamsType() != ConstantEnum.ParamsType.xml.getValue()) {// 除去xml字段，非xml字段才转换
                    xml = Operator.replace(xml, entity.getParamName(),
                            getValueXMLandJson(request, entity, requestParams,paramJson));
                }
                if (isFather) {
                    if (xml != null) {
                        xml = replaceDateExpression(xml);
                    }else {
                        LOGGER.debug(entity.getParamName() + "xml值为null");
                    }
                }
            }
        } else {// 一般情况
            //替换其他村镇银行常量帐号
            if (accountMap == null) {
                for (Object object : backParams) {
                    entity = (GrandEntityBackParam) object;
                    Integer location = entity.getParamsLocation();
                    putValue2MapWithLocation(headerVariables,bodyVariables,queryVariables,location,
                            entity.getParamName(),getValue(request, entity, requestParams,paramJson));
                }
            }else {
                LOGGER.debug("替换常量值参数");
                for (Object object : backParams) {
                    entity = (GrandEntityBackParam) object;
                    Integer location = entity.getParamsLocation();
                    putValue2MapWithLocation(headerVariables,bodyVariables,queryVariables,location,
                            entity.getParamName(),getChangeValue(request, entity, requestParams,accountMap,paramJson));
                }
            }
            if (encodeParam != null) {
                // 变量值替换
                if (!headerVariables.isEmpty()) {
                    for (String key : headerVariables.keySet()) {
                        encodeValue = Operator.replace(encodeValue, key, headerVariables.get(key));
                    }
                }
                if (!bodyVariables.isEmpty()) {
                    for (String key : bodyVariables.keySet()) {
                        encodeValue = Operator.replace(encodeValue, key, bodyVariables.get(key));
                    }
                }
                if (!queryVariables.isEmpty()) {
                    for (String key : queryVariables.keySet()) {
                        encodeValue = Operator.replace(encodeValue, key, queryVariables.get(key));
                    }
                }
                // 如果有时间戳就替换时间戳
                if (encodeValue.contains("${timestamp}")) {
                    encodeValue = Operator.replace(encodeValue, "timestamp",
                            String.valueOf(System.currentTimeMillis()));
                }
                // 有日期就替换日期
                if (encodeValue.contains("${yyyyMMdd}")) {
                    encodeValue = Operator.replace(encodeValue, "yyyymmdd",
                            DateUtil.getToday().replace("-", ""));
                }
                if (encodeValue.contains("${yyyy-MM-dd}")) {
                    encodeValue = Operator.replace(encodeValue, "yyyymmdd", DateUtil.getToday());
                }
                //自定义日期格式进行日期替换，配置格式："字段名":"${timeReplacePattern}${@yyMMdd@}"
                if (isFather) {
                    encodeValue = replaceTimePattern(encodeValue);
                }
                // 执行加密
                encodeValue = Operator.MD5ByStringArray(encodeValue.split("[,，]"));
                // 放入相应map中
                putValue2MapWithLocation(headerVariables, bodyVariables, queryVariables,
                        encodeLocation, encodeParam, encodeValue);

            }
        }

        if (isFather) {
            return new MainEntity.Params(headerVariables, queryVariables, bodyVariables, json, xml);
        }else {
            return new ChildEntity.Params(headerVariables, queryVariables, bodyVariables, json,xml);
        }
    }

    /**
     * 2019-05-27
     * 根据参数位置放置请求参数
     * @param headerVariables
     * @param bodyVariables
     * @param queryVariables
     * @param location
     * @param mapKey
     * @param mapValue
     */
    private void putValue2MapWithLocation(Map<String, String> headerVariables, Map<String, String> bodyVariables,
                                          Map<String, String> queryVariables, Integer location,
                                          String mapKey, String mapValue) {
        if (ConstantEnum.ParamsLocation.head.getValue() == location) {
            headerVariables.put(mapKey, mapValue);
        } else if (ConstantEnum.ParamsLocation.body.getValue() == location) {
            bodyVariables.put(mapKey, mapValue);
        } else {
            queryVariables.put(mapKey, mapValue);
        }
    }

    /**
     * 2019-05-27
     * 获取传入参数
     * @param request
     * @param grand
     * @param requestParamsVersions
     * @return
     */
    private String getValueXMLandJson(HttpServletRequest request, GrandEntityBackParam grand,
                                      List<RequestParamsVersions> requestParamsVersions,JSONObject paramJson) {
        Integer requestParamsId = grand.getRequestParamsId();
        RequestParamsVersions frontPara = null;
        for (RequestParamsVersions reqParam : requestParamsVersions) {
            if (reqParam.getRequestParamsId().equals(requestParamsId)) {
                frontPara = reqParam;
                break;
            }
        }
        if (frontPara == null)
            return null; // 没有查询到id对应的RequestParamsVersions,直接返回null

        String value = getRequestValue(request, frontPara,paramJson); // 取传入的参数
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            String defaultValue = frontPara.getParamsDefaultValue();
            return StringUtils.isBlank(defaultValue) ? "" : defaultValue;// 取默认值
        }
    }

    /**
     * 2019-05-27
     * 获取常量参数和请求参数
     * @param request
     * @param back
     * @param requestParamsVersions
     * @return
     */
    private String getValue(HttpServletRequest request, GrandEntityBackParam back,
                            List<RequestParamsVersions> requestParamsVersions, JSONObject paramJson) {
        String value = back.getParamValue();
        // 常量参数
        if (ConstantEnum.ParamsType.constant.getValue() == back.getParamsType()) {
            return value;
        }
        return getValueXMLandJson(request, back, requestParamsVersions,paramJson);
    }

    private String getChangeValue(HttpServletRequest request, GrandEntityBackParam back,
                                  List<RequestParamsVersions> requestParamsVersions, Map<String, Object> map,JSONObject paramJson) {
        String value = back.getParamValue();
        // 常量参数
        if (ConstantEnum.ParamsType.constant.getValue() == back.getParamsType()) {
            String paramName = back.getParamName();
            if (map.containsKey(paramName)) {
                return (String) map.get(paramName);
            }
            return value;
        }
        return getValueXMLandJson(request, back, requestParamsVersions,paramJson);
    }

    private String getRequestValue(HttpServletRequest request, RequestParamsVersions para, JSONObject paramJson) {
        Integer location = para.getParamsLocation();
        String name = para.getParamName();
        if (ConstantEnum.ParamsLocation.head.getValue() == location) {
            return request.getHeader(name);
        } else if (ConstantEnum.ParamsLocation.body.getValue() == location) {
            return paramJson.getString(name);
        } else {
            return paramJson.getString(name);
        }
    }

    @Override
    public String doRequest(MainEntity mainEntity, MainEntity.Params params, String appkey, String redisKeyStr, String retryFlag, int retryCount, String type) {

        ApiInfoVersions api = mainEntity.getApiInfo();
        String protocol = null;
        Integer protocolType = api.getBackendProtocolType();
        protocol = takeProtocol(protocolType);
        //上游接口唯一标识
        String uniqueUuid = api.getUniqueUuid();
        String url;
        url = getUrlByEnvironment(api);
        // method
        String method = null;
        if (1 == protocolType || 2 == protocolType) {
            method = api.getBackEndHttpMethod() == 1 ? ConstantEnum.HttpMethod.get.getName() : ConstantEnum.HttpMethod.post.getName();
        }
        LOGGER.debug("请求信息:\r\nuniqueUuid: {} \r\nProtocol: {} url: {}\r\nMethod: {}\r\nHeaders: {}\r\nQuerys: {}\r\nBodys: {}\r\nJSON: {}\r\nXML: {}",
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
//        // cacheTime
//        Integer cacheTime = Operator.cacheTime(api.getCacheUnit(), api.getCacheNo());
//        // param的md5值
//        String md5 = MD5Utils.MD5(redisKeyStr);
//        LOGGER.debug("md5:  {}", md5);
//        AsyncModel async = AsyncCollectionUtil.getAsyncModel();
        String result = null;
        // 有缓存策略，查缓存
//        LOGGER.info("是否从redis缓存中取数据：{}", cacheTime != -1);
//        result = pullResultFromCacheJsonNoHtml(appkey, uniqueUuid, cacheTime, md5, async, true);
//        LOGGER.info("查询缓存结果{},{}",api.getApiName(),result);
//        if (StringUtils.isNotBlank(result)) return result;

        // 没缓存策略，走正常流程

        result = sendRequest(method, url, params.getBodyVariables(),params.getQueryVariables(),
                params.getHeaderVariables(),params.getJson(),params.getXml(),
                timeout, protocol, retryFlag, retryCount);
//        async.setCached(2);
//        if (StringUtils.isNotBlank(params.getJson()) && params.getJson().contains("innerInterId")) {
//            LOGGER.info("result---,{}", result);
//            result = JDStrategy.getResult(result, JDEncriptUtil.getPassword());
//        }
        result = ExtractUtil.xml2Json(result).toString();
        //缓存
//        putAsyncOrResultToCache(result,async,appkey,md5, cacheTime,uniqueUuid,type,cacheTime != -1);
//        AsyncCollectionUtil.putAsyncModel(async);
        return result;
    }

    /**
     * 根据是否缓存获取结果,是否转换成json
     * @param appkey
     * @param uniqueUuid
     * @param cacheTime
     * @param md5
     * @param async
     * @param toJson
     * @return
     */
    private String pullResultFromCacheJsonNoHtml(Integer apiId, String appkey, String uniqueUuid, Integer cacheTime, String md5, AsyncModel async, boolean toJson) {
        String result;
        if (cacheTime != -1) {
            result = getResultFromCache(apiId,appkey, md5, uniqueUuid);
            if (StringUtils.isNotBlank(result)) {
                asyncSetAndPut(async);
                if (toJson) {
                    return ExtractUtil.xml2Json(result).toString();
                }else {
                    return ExtractUtil.noHtml2Json(result).toString();
                }
            }
        }
        return null;
    }
    /**
     * 设置异步收集字段，然后根据策略是否存入缓存
     * @param result
     * @param async
     * @param appkey
     * @param md5
     * @param cacheTime
     * @param uniqueUuid
     * @param type
     * @param isCache
     */

    private void putAsyncOrResultToCache(Integer apiId,String result, AsyncModel async, String appkey, String md5,
                                         Integer cacheTime, String uniqueUuid, String type, boolean isCache) {

        if (result!=null && isCache && OperatorCache.resultIsToCache(result, type)) {
            async.setSourceStatus(1);
            async.setTargetStatus(1);
            async.setCached(1);
            setResultToCache(apiId,appkey, md5, result, cacheTime, uniqueUuid, type);
        }else{
            async.setSourceStatus(2);
            async.setTargetStatus(2);
            async.setCached(2);
        }
    }

    /**
     * 2019-05-28
     * 发送请求method
     * @param method
     * @param url
     * @param bodyVariables
     *@param queryVariables
     * @param headerVariables
     * @param json
     * @param xml
     * @param timeout
     * @param protocol
     * @param retryFlag
     * @param retryCount     @return
     */
    private String sendRequest(String method, String url, Map<String, String> bodyVariables,
                               Map<String, String> queryVariables, Map<String, String> headerVariables,
                               String json, String xml, Long timeout, String protocol,
                               String retryFlag, int retryCount) {
        String result = null;
        if (ConstantEnum.HttpMethod.get.getName().equals(method)) {
            LOGGER.debug("Starting GET request");
            result = OkHttp.get(url, queryVariables, headerVariables, timeout,
                    protocol, retryFlag, retryCount,false);
        } else if (ConstantEnum.HttpMethod.post.getName().equals(method)) {
            if (json != null) {
                LOGGER.debug("Starting POST request");
                result = OkHttp.postJson(url, queryVariables, json,
                        headerVariables, timeout, protocol, retryFlag, retryCount,false);
            } else if (xml != null) {
                LOGGER.debug("Starting POST XML request");
                result = OkHttp.postXml(url, queryVariables, xml,
                        headerVariables, timeout, protocol, retryFlag, retryCount,false);
            } else {
                LOGGER.debug("Starting POST KV request");
                result = OkHttp.postKV(url, queryVariables, bodyVariables,
                        headerVariables, timeout, protocol, retryFlag, retryCount,false);
            }
        } else if (isSocket(ConstantEnum.ProtocolType.valueOf(protocol).getValue())) {
            String[] arr = splitHostAndPort(url);
//            result = SocketUtil.send2TcpServer(xml, arr[0], Integer.parseInt(arr[1]), timeout.intValue());
        }
        return result;
    }

    /**
     * 2019-05-28
     * 更新异步字段
     * @param async
     */
    private void asyncSetAndPut(AsyncModel async) {
        async.setCached(1);
        async.setSourceStatus(2);
        async.setTargetStatus(1);
        AsyncCollectionUtil.putAsyncModel(async);
    }

    /**
     * 2019-05-28
     * 获取请求协议
     * @param protocol
     * @return
     */
    private String takeProtocol(Integer protocol) {
        if (1 == protocol) {
            return ConstantEnum.ProtocolType.http.getName();
        }else if (2 == protocol) {
            return ConstantEnum.ProtocolType.https.getName();
        }else if (isSocket(protocol)) {
            return ConstantEnum.ProtocolType.socket.getName();
        }else {
            return null;
        }
    }

    @Override
    public ChildEntity getChildEntity(String uniqueUuid, String versionId) {
        // 加缓存
        ChildEntity childEntity = getFromCache(versionId, uniqueUuid);
        if (childEntity == null) {
            childEntity = new ChildEntity();
            childEntity.setApiInfo(mainService.selectApiInfo(versionId));
            ApiRateDistributeWithBLOBs apiRateDistribute = childrenService.childApi(uniqueUuid);
            childEntity.setChildApi(apiRateDistribute);
            childEntity.setChildParams(childrenService.childParams(apiRateDistribute.getId()));
            childEntity.setRequestParams(childrenService.requestParams(versionId));

            setToCache(versionId, uniqueUuid, childEntity);
        }

        // 不加缓存
//		ChildEntity childEntity = new ChildEntity();
//		childEntity.setApiInfo(mainService.selectApiInfo(versionId));
//		ApiRateDistributeWithBLOBs apiRateDistribute = childrenService.childApi(uniqueUuid);
//		childEntity.setChildApi(apiRateDistribute);
//		childEntity.setChildParams(childrenService.childParams(apiRateDistribute.getId()));
//		childEntity.setRequestParams(childrenService.requestParams(versionId));

        return childEntity;
    }

    private void setToCache(String versionId, String uniqueUuid, ChildEntity apiInfo) {
        String key = getKey(versionId + uniqueUuid);
        try {
            redisService.set(key, apiInfo);
        } catch (Exception e) {

            LOGGER.error(LogPojo.getErrorLogMsg("存入Redis出错", apiInfo, e));
        }
    }

    private ChildEntity getFromCache(String versionId, String uniqueUuid) {
        String key = getKey(versionId + uniqueUuid);
        try {
            return redisService.get(key, ChildEntity.class);
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("存入Redis出错", key, e));
        }
        return null;
    }

    @Override
    public ChildEntity.Params handleChildEntity(ChildEntity childEntity,
                                                HttpServletRequest request, Map<String,Object> accountMap,JSONObject paramJson) {
        Map<String, String> headerVariables = new HashMap<String, String>(); // header中的参数
        Map<String, String> queryVariables = new LinkedHashMap<String, String>(); // query的参数,get方式有
        Map<String, String> bodyVariables = new LinkedHashMap<String, String>(); // body中的参数,post方式有

        List<BackendDistributeParams> backParams = childEntity.getChildParams();
        return  (ChildEntity.Params) convertParamsAbs(backParams,request, headerVariables, queryVariables,
                bodyVariables, childEntity.getRequestParams(),false, accountMap,paramJson);
    }

    @Override
    public String doRequest(ChildEntity childEntity, ChildEntity.Params params, String appkey, String redisKeyStr, String retryFlag, int retryCount, String type) {
        ApiRateDistribute api = childEntity.getChildApi();
        Integer apiId = api.getApiId();
        ApiInfoVersions main = childEntity.getApiInfo();
        // url
        String url = getChildUrlByEnvironment(api);
        // http?https
        String protocol = api.getBackendProtocolType() == 1 ? ConstantEnum.ProtocolType.http.getName()
                : ConstantEnum.ProtocolType.https.getName();
        // method
        String method = api.getBackEndHttpMethod() == 1 ? ConstantEnum.HttpMethod.get.getName() : ConstantEnum.HttpMethod.post.getName();
        LOGGER.debug("请求信息:\r\nProtocol: {} url: {}\r\nMethod: {}\r\nHeaders: {}\r\nQuerys: {}\r\nBodys: {}\r\nJSON: {}\r\nXML: {}",
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
        // cacheTime
        Integer cacheTime = Operator.cacheTime(main.getCacheUnit(), main.getCacheNo());
        // param的md5值
        String md5 = MD5Utils.MD5(redisKeyStr);
        //上游接口唯一标识
        String uniqueUuid = api.getUniqueUuid();

        AsyncModel async = AsyncCollectionUtil.getAsyncModel();

        String result = null;
        // 有缓存策略，查缓存
        result = pullResultFromCacheJsonNoHtml(apiId,appkey, uniqueUuid, cacheTime, md5, async, true);
        if (StringUtils.isNotBlank(result)) {
            LOGGER.info("查询缓存结果{},{}",api.getApiId(),result);
            return result;
        }
        // 没缓存策略，走正常流程
        result = sendRequest(method, url, params.getBodyVariables(),params.getQueryVariables(),
                params.getHeaderVariables(),params.getJson(),params.getXml(),
                timeout, protocol, retryFlag, retryCount);

        async.setCached(2);
        /*if (StringUtils.isNotBlank(params.getJson()) && params.getJson().contains("innerInterId")) {
            result = JDStrategy.getResult(result, JDStrategy.getPassword());
        }*/
        result = ExtractUtil.xml2Json(result).toString();
        //是否存入缓存
        putAsyncOrResultToCache(apiId,result,async,appkey,md5,cacheTime,uniqueUuid,type,cacheTime != -1);
        AsyncCollectionUtil.putAsyncModel(async);
        return result;
    }



    @Override
    public String removeRedisKey(ApiInfoVersions api, String params, String appKey) {
        String uniqueUuid = api.getUniqueUuid();
        Integer apiId = api.getApiId();
        String md5 = MD5Utils.MD5(params);
        String key = getResultKey(apiId,appKey, md5, uniqueUuid);
        Long l = redisService.delete(key);
        LOGGER.debug("Successfully delete redis key : {}, {}", key, l);
        return key;
    }


    private boolean isSocket(Integer protocolType) {
        return 3 == protocolType;
    }

    private String[] splitHostAndPort(String url) {
        return url.split(":");
    }

    private String replaceDateExpression(String encodeValue) {
        if (encodeValue.indexOf("${timestamp}") > -1) {
            encodeValue = Operator.replace(encodeValue, "timestamp",
                    String.valueOf(System.currentTimeMillis()));
        }
        // 有日期就替换日期
        if (encodeValue.indexOf("${yyyyMMdd}") > -1) {
            encodeValue = Operator.replace(encodeValue, "yyyyMMdd", DateUtil.getToday().replace("-", ""));
        }
        if (encodeValue.indexOf("${yyyy-MM-dd}") > -1) {
            encodeValue = Operator.replace(encodeValue, "yyyy-MM-dd", DateUtil.getTodayDate());
        }
        if (encodeValue.indexOf("${hhmmss}") > -1) {
            encodeValue = Operator.replace(encodeValue, "hhmmss", DateUtil.getTodayTime());
        }
        encodeValue = replaceTimePattern(encodeValue);
        return encodeValue;
    }

    private String replaceTimePattern(String encodeValue) {
        if (encodeValue.indexOf("${timeReplacePattern}") > -1) {
            int start = encodeValue.indexOf("@") + 1;
            int end = encodeValue.lastIndexOf("@");
            String pattern = encodeValue.substring(start, end);
            encodeValue = Operator.replace(encodeValue, "timeReplacePattern", DateUtil.getTodayWithPattern(pattern));
            encodeValue = Operator.replace(encodeValue, "@" + pattern + "@", "");
        }
        return encodeValue;
    }

    /**
     * 根据环境获取url，请求为socket时，path为空
     *
     * @param apiInfo
     * @return
     */
    public String getUrlByEnvironment(ApiInfoVersions apiInfo) {
        String environment = gatewayClientService.getEnvironment();
        Integer protocolType = apiInfo.getBackendProtocolType();
        String url="";
        if (com.lemon.common.vo.Constant.GATEWAY_CLIENT_SERVICE_B.equals(environment))
            url = isSocket(protocolType)? apiInfo.getBackEndAddressB():(apiInfo.getBackEndAddressB() + apiInfo.getBackEndPath());
        else
            url = isSocket(protocolType)? apiInfo.getBackEndAddress():(apiInfo.getBackEndAddress() + apiInfo.getBackEndPath());
        LOGGER.debug("environment:{},url:{}",environment,url);
        return url;
    }

    private String getChildUrlByEnvironment(ApiRateDistribute apiInfo) {
        String environment = gatewayClientService.getEnvironment();
        Integer protocolType = apiInfo.getBackendProtocolType();
        String url="";
        if (com.lemon.common.vo.Constant.GATEWAY_CLIENT_SERVICE_B.equals(environment))
            url = isSocket(protocolType)? apiInfo.getBackEndAddressB():(apiInfo.getBackEndAddressB() + apiInfo.getBackEndPath());
        else
            url = isSocket(protocolType)? apiInfo.getBackEndAddress():(apiInfo.getBackEndAddress() + apiInfo.getBackEndPath());
        LOGGER.debug("environment:{},ul:{}",environment,url);
        return url;

    }
    /**
     * 打印前端参数。方便风控查询
     */
    @Override
    public MainEntity.Params  getRequestParams(HttpServletRequest request,MainEntity mainEntity,JSONObject paramJson) {
        Map<String, String> headerVariables = new HashMap<>();
        Map<String, String> queryVariables = new HashMap<>();
        Map<String, String> bodyVariables = new HashMap<>();
        for (RequestParamsVersions para:mainEntity.getRequestParams()){
            Integer location = para.getParamsLocation();
            String name = para.getParamName();
            if (ConstantEnum.ParamsLocation.head.getValue() == location) {
                headerVariables.put(name,StringUtils.isNotBlank(request.getHeader(name))?request.getHeader(name):para.getParamsDefaultValue());
            } else if (ConstantEnum.ParamsLocation.body.getValue() == location) {
                bodyVariables.put(name,StringUtils.isNotBlank(paramJson.getString(name))?paramJson.getString(name):para.getParamsDefaultValue());
            } else {
                queryVariables.put(name,StringUtils.isNotBlank(paramJson.getString(name))?paramJson.getString(name):para.getParamsDefaultValue());
            }
        }
        LOGGER.info("前端参数信息:\r\nHeaders: {}\r\nQuerys: {}\r\nBodys: {}",headerVariables,queryVariables,bodyVariables);
        return new MainEntity.Params(headerVariables, queryVariables, bodyVariables);
    }
}
