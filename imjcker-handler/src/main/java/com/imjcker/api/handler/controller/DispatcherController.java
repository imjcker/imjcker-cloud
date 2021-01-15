package com.imjcker.api.handler.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.api.common.util.MD5Utils;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.vo.LogPojo;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.api.handler.exception.ThirdBusinessException;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.InterfaceCountModel;
import com.imjcker.api.handler.service.AsyncMQService;
import com.imjcker.api.handler.service.MicroChargeService;
import com.imjcker.api.handler.strategy.ApiStrategy;
import com.imjcker.api.handler.strategy.ApiStrategyFactory;
import com.imjcker.api.handler.strategy.ApiStrategyMapping;
import com.imjcker.api.handler.util.*;
import com.imjcker.api.handler.exception.ThirdBusinessException;
import com.imjcker.api.handler.service.ClientService;
import com.imjcker.api.handler.service.GatewayClientService;
import com.imjcker.api.handler.service.MainService;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.ChildEntity;
import com.imjcker.api.handler.model.InterfaceCountModel;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.plugin.queue.ApiHandler;
import com.imjcker.api.handler.po.ApiInfoResponse;
import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.service.AsyncMQService;
import com.imjcker.api.handler.service.GatewayExitService;
import com.imjcker.api.handler.service.MicroChargeService;
import com.imjcker.api.handler.service.RabbitMQService;
import com.imjcker.api.handler.strategy.ApiStrategy;
import com.imjcker.api.handler.strategy.ApiStrategyFactory;
import com.imjcker.api.handler.strategy.ApiStrategyMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DispatcherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherController.class);

    @Value("${spring.application.name}")
    private String serverName;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MainService mainService;

    @Autowired
    private AsyncMQService asyncMQService;
    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private GatewayClientService gatewayClientService;

    @Autowired
    private GatewayExitService gatewayExitService;
    @Autowired
    private MicroChargeService microChargeService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_CACHE = "API_CACHE";

    private static final String ZIPKIN_ORDERID = "orderId";

    @RequestMapping(value = "/**")
    public Object handle(HttpServletRequest request) {

        String uid = request.getHeader(ZuulHeader.PARAM_KEY_ORDER_ID);
        try {
            long startTime = System.currentTimeMillis();
            // 在header中传参数API_HEALTH,来判断健康
            String apiHealth = request.getHeader("API_HEALTH");
            if (StringUtils.isNotBlank(apiHealth)) {
                return ResponseBuilder.builder()
                        .uid(uid)
                        .errorCode(ResultStatusEnum.TFB_SUCCESS.getCode())
                        .errorMsg(ResultStatusEnum.TFB_SUCCESS.getMessage())
                        .build();
            }
            String versionId = request.getHeader(ZuulHeader.PARAM_KEY_API_VERSION_ID);
            String oderIdCreateTime = request.getHeader(ZuulHeader.PARAM_UID_CREATE_TIME);
            String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
            String parentUid = StringUtils.isBlank(request.getHeader("parentUid")) ? request.getParameter("parentUid") : request.getHeader("parentUid");

            String inmgrCode = StringUtils.isBlank(request.getHeader("inmgrCode")) ? request.getParameter("inmgrCode") : request.getHeader("inmgrCode");
            Boolean transCodeFlag = true;//转码标识

            Integer apiId = Integer.parseInt(request.getHeader(ZuulHeader.PARAM_API_ID));
            Integer weight = Integer.parseInt(request.getHeader(ZuulHeader.PARAM_WEIGTH));
            String uniqueUuid = request.getHeader(ZuulHeader.PARAM_UNIQUE_UUID);
            String accountConfig = request.getHeader(ZuulHeader.PARAM_AGENCY_API_ACCOUNT);
            boolean debugEnabled = LOGGER.isDebugEnabled();
            if (debugEnabled) {
                LOGGER.debug("----start apiCache");
            }
            // API_CACHE
            String apiCache = request.getHeader(API_CACHE);
            LogPojo.init(uid, LogPojo.getServerId(request), serverName, appKey, versionId);
            // 收集异步字段
            AsyncModel async = new AsyncModel();
            async.setApiId(apiId.longValue());
            async.setCompanyTag(appKey);
            async.setUid(uid);
            async.setParentUid(parentUid);
            async.setInitParams(RequestUtil.getRequestParams(request));
            async.setOrderIdCreateTime(oderIdCreateTime);
            // 接口调用统计字段
            InterfaceCountModel countModel = new InterfaceCountModel();
            countModel.setApiId(apiId.longValue());
            countModel.setAppKey(appKey);
            countModel.setUid(uid);
            countModel.setCreateTime(oderIdCreateTime);
            AsyncCollectionUtil.putCountModel(countModel);

            ApiInfoResponse apiInfoResponse = mainService.findApiInfoById(apiId);
            LOGGER.debug("apiId={},apiName={},jsonConfig={}", apiInfoResponse.getApiId(), apiInfoResponse.getApiName(), apiInfoResponse.getJsonConfig());
            String jsonConfig = apiInfoResponse.getJsonConfig();

            // 负载均衡，决定此次访问哪个上游 1,swdsfdfdf：表示主接口 2,swdsfdfdf：表示子接口
            String host = clientService.getHost(apiId, uniqueUuid, weight);

            if (host == null) {
                return ResponseBuilder.builder()
                        .uid(uid)
                        .errorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode())
                        .errorMsg("无可用的上游接口！请检查权重配置！")
                        .build();
            }
            async.setInterfaceUuid(host.split(",")[1]);
            AsyncCollectionUtil.putAsyncModel(async);
            // 公共的ApiInfoVersions
            final ApiInfoVersions apiInfo;
            // 公共的返回结果
            JSONObject thirdResult = new JSONObject();
            // 码值转换后的结果
            String configJson = "";
            // 初始三方返回结果
            String result = "";
            String retryFlag = "false";
            int retryCount = 0;

            // 走主接口,不进if就走子接口
            if ("1".equals(host.split(",")[0])) {
                // 获取主接口参数信息
                MainEntity mainEntity = clientService.getMainEntity(versionId);
                apiInfo = mainEntity.getApiInfo();
                LOGGER.info("开始调用:{},主接口id:{},parentUid={},appKey={},jsonconfig={}", apiInfo.getApiName(), apiInfo.getApiId(), parentUid, appKey, apiInfoResponse.getJsonConfig());
                AsyncCollectionUtil.putAsyncUrl(apiInfo.getHttpPath());
                AsyncCollectionUtil.putCharge(apiInfo.getCharge());

                String version = getVersion(request);
                JSONObject paramJson;
                if (com.imjcker.api.common.vo.Constant.REQUEST_VERSION_NOW.equals(version)) {
                    //流中获取处理好的参数
                    String paramStr = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

                    if (StringUtils.isNotBlank(paramStr)) {
                        paramJson = JSON.parseObject(paramStr);
                    } else {
                        paramJson = new JSONObject();
                    }
                } else {
                    paramJson = new JSONObject();
                    Enumeration enu = request.getParameterNames();
                    while (enu.hasMoreElements()) {
                        String paraName = (String) enu.nextElement();
                        paramJson.put(paraName, request.getParameter(paraName));
                    }
                }

                //打印前端参数
                MainEntity.Params params_before = clientService.getRequestParams(request, mainEntity, paramJson);
                mainEntity.setParams(params_before);
                //转码选择
                configJson = mainEntity.getApiInfo().getResponseTransParam();
                transCodeFlag = RequestUtil.isTransCode(inmgrCode, configJson);

                //替换jsonconfig（村镇银行账户替换）
                Map<String, Object> accountMap = null;
                if (StringUtils.isNotBlank(accountConfig) && StringUtils.isNotBlank(jsonConfig)) {
                    accountMap = objectMapper.readValue(accountConfig, new TypeReference<Map<String, Object>>() {
                    });
                    //替换jsonConfig下级银行的账户参数
                    JSONObject jsonObjectConfig = JSONObject.parseObject(jsonConfig);
                    accountMap.forEach((key, value) -> {
                        if (jsonObjectConfig.containsKey(key)) {
                            jsonObjectConfig.put(key, value);
                        }
                    });
                    jsonConfig = jsonObjectConfig.toJSONString();
                    LOGGER.debug("替换之后的jsonConfig: {}", jsonConfig);
                }

                // 请求参数转换
                MainEntity.Params params = clientService.handleMainEntity(mainEntity, request, accountMap, paramJson);
                // orderId方便风控排查
                String orderId = "";
                if (containsOrderIdParam(params.getBodyVariables())) {
                    orderId = params.getBodyVariables().get(ZIPKIN_ORDERID);
                }
                String endPath = (apiInfo.getBackEndPath() == null) ? "" : apiInfo.getBackEndPath();//用于md5加密
                //获取redis加密参数值，去除变量类型（时间）
                //redis不参与加密字段去除改造
                Map<String, Object> map = null;
                if (StringUtils.isNotBlank(jsonConfig)) {
                    map = objectMapper.readValue(jsonConfig, new TypeReference<Map<String, Object>>() {
                    });
                }
                Object keyList = map == null ? null : map.getOrDefault(Constant.KEY_LIST, null);
                Object keyPosition = map == null ? null : map.getOrDefault(Constant.KEY_POSITION, null);
                String handleRedisParams = RedisKeyUtils.handleRedisKey(params, keyList == null ? null : keyList.toString(), keyPosition == null ? null : keyPosition.toString(), Constant.KEY_LIST_SEPERATOR);
                String redisParams = new StringBuilder(endPath).append(handleRedisParams).toString();
                LOGGER.debug("redisParam:{}", redisParams);
                LOGGER.info("orderId:{}, 后端请求参数:{} \n", orderId, params.toString());
                AsyncCollectionUtil.putAsyncParams(params);

                //清除缓存
                if (API_CACHE.equals(apiCache)) {
                    String key = clientService.removeRedisKey(mainEntity.getApiInfo(), redisParams, appKey);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("redisKey", key);
                    return jsonObject;
                }
                // 有缓存策略，查缓存
                Integer cacheTime = Operator.cacheTime(apiInfo.getCacheUnit(), apiInfo.getCacheNo());
                // param的md5值
                String md5 = MD5Utils.MD5(redisParams);
                String result_cache = RequestUtil.pullResultFromCacheJsonNoHtml(apiId, appKey, uniqueUuid, cacheTime, md5, async);
                LOGGER.info("查询缓存结果={}", result_cache);
                //异步结果推送
                if (StringUtils.isNotBlank(result_cache)) {
                    result = result_cache;
                } else {
                    String environment = gatewayClientService.getEnvironment();
                    if (StringUtils.isNotBlank(jsonConfig)) {
                        if (map == null)
                            map = objectMapper.readValue(jsonConfig, new TypeReference<Map<String, Object>>() {
                            });
                        map.put("uid", uid);

                        //清洗接口标识，调用清洗接口时，约定在header中传递uid到7301
                        if (map.containsKey("isClearInterface") && "Y".equals(map.get("isClearInterface"))) {
                            LOGGER.debug("清洗接口,header中传入uid={}", uid);
                            params.getHeaderVariables().put("uid", uid);
                        }
                        //重试标志和次数
                        retryFlag = map.getOrDefault("retry_flag", "false").toString();
                        retryCount = Integer.parseInt(map.getOrDefault("retry_count", 0).toString());

                        if (!map.containsKey("type")) {
                            map.put("type", Constants.TFB_TYPE);
                        }
                    } else {
                        if (map == null)
                            map = new HashMap<>();
                        map.put("type", Constants.TFB_TYPE);
                    }
                    map.put("uid", uid);
                    map.put("environment", gatewayClientService.getEnvironment());// 借助jsonConfig传递环境变量到api-handler

                    String group = (String) map.get("type");
                    ApiStrategy apiStrategy = null;
                    try {
                        apiStrategy = ApiStrategyFactory.getStrategy(ApiStrategyMapping.getEnumOfGroup(group));
                    } catch (Exception e) {
                        LOGGER.debug("type: {} 没有对应的枚举值,采用默认第三方策略", group);
                    }
                    // 如果没有,走CommonStrategy
                    if (apiStrategy == null)
                        apiStrategy = ApiStrategyFactory.getStrategy(ApiStrategyMapping.COMMON);
                    LOGGER.debug("转换参数策略: {}", apiStrategy.getClass().getSimpleName());
//                    JSONObject object = apiStrategy.handleParamsForExit(mainEntity.getApiInfo(), params, retryFlag,
//                            retryCount, map, environment);
                    String url = RequestUtil.getUrlByEnvironment(mainEntity.getApiInfo(), environment);
                    String jsonParam = apiStrategy.handleParams(params, mainEntity, map, url);

                    JSONObject object = JSONObject.parseObject(jsonParam);
                    object.put("sourceName", group);
                    if (debugEnabled)
                        LOGGER.debug("封装参数: {}", object.toJSONString());
                    String s = gatewayExitService.exitRequest(object);
                    // json格式key不为null,value为null时 ,同样输出 key,value, fastjson默认不输出
                    result = JSON.toJSONString(apiStrategy.handleResult(s), SerializerFeature.WriteMapNullValue);
                    //设置缓存
                    RequestUtil.putAsyncOrResultToCache(apiId, result, async, appKey, md5, cacheTime, uniqueUuid, map.get("type").toString(), cacheTime != -1);
                    AsyncCollectionUtil.putAsyncModel(async);
                }
                AsyncCollectionUtil.putAsyncInitResult(result);
            } else {
                // 获取子接口参数信息
                LOGGER.debug("进入子接口");
                ChildEntity childEntity = clientService.getChildEntity(uniqueUuid, versionId);
                apiInfo = childEntity.getApiInfo();
            }
            LOGGER.debug("第三方原始报文:{}", result);
            //转码
            ResultUtil.handleResult(thirdResult, uid, configJson, result, transCodeFlag);
            //发送MQ
            final AsyncModel asyncModel = AsyncCollectionUtil.getAsyncModel();
            //异步发送MQ, TODO 暂时和异步请求用一个消息队列,以后优化
            new ApiHandler() {
                @Override
                public void handle() {
                    asyncMQService.sendMQ(startTime, countModel, apiInfo, thirdResult, asyncModel);
                }
            }.putQueue();

            return thirdResult;
        } catch (ThirdBusinessException e) {
            LOGGER.error("发生数据源异常: {}", e.getMessage());
            return ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getCode())
                    .errorMsg(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getMessage())
                    .build();
        } catch (Exception e) {
            LOGGER.error("DispatcherController error------------>", e);
            return ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode())
                    .errorMsg(ResultStatusEnum.TFB_INTERNAL_ERROR.getMessage())
                    .build();
        }
    }

    private boolean containsOrderIdParam(Map<String, String> bodies) {

        return bodies.containsKey(ZIPKIN_ORDERID);
    }

    /**
     * 根据环境获取url
     *
     * @param apiInfo
     * @return
     */
    public String getUrlByEnvironment(ApiInfoVersions apiInfo) {
        String environment = gatewayClientService.getEnvironment();
        if (com.imjcker.api.common.vo.Constant.GATEWAY_CLIENT_SERVICE_B.equals(environment))
            return apiInfo.getBackEndAddressB() + apiInfo.getBackEndPath();
        else
            return apiInfo.getBackEndAddress() + apiInfo.getBackEndPath();
    }

    private String getParamStr(ServletInputStream inputStream) throws IOException {
        StringBuffer data = new StringBuffer();
        BufferedInputStream buf = null;
        buf = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int a;
        while ((a = buf.read(buffer)) != -1) {
            data.append(new String(buffer, 0, a, StandardCharsets.UTF_8));
        }

        return data.toString();
    }

    /**
     * 获取请求版本号
     */
    private String getVersion(HttpServletRequest request) {
        String version = null;
        if (StringUtils.isNotBlank(request.getHeader(ZuulHeader.REQUEST_VERSION))) {
            version = request.getHeader(ZuulHeader.REQUEST_VERSION);
            return version;
        }
        if (StringUtils.isNotBlank(request.getParameter(ZuulHeader.REQUEST_VERSION))) {
            version = request.getParameter(ZuulHeader.REQUEST_VERSION);
            return version;
        }
        if (StringUtils.isBlank(version)) {
            version = com.imjcker.api.common.vo.Constant.REQUEST_VERSION_OLD;
        }
        return version;
    }

}
