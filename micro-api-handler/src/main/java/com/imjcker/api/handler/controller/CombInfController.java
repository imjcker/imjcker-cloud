package com.imjcker.api.handler.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.imjcker.api.common.util.MD5Utils;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.RedisClusterUtils;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.api.handler.exception.ThirdBusinessException;
import com.imjcker.api.handler.util.*;
import com.imjcker.api.handler.exception.ThirdBusinessException;
import com.imjcker.api.handler.service.CombInfService;
import com.imjcker.api.handler.service.EnvironmentService;
import com.imjcker.api.handler.service.ParamsService;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.ApiInfoResponse;
import com.imjcker.api.handler.po.ApiInfoVersionsWithBLOBs;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Author WT
 * @Date 9:06 2019/8/20
 * @Version CombInfController v1.0
 * @Desicrption
 */
@RestController
public class CombInfController {

    private static final Logger logger = LoggerFactory.getLogger(CombInfController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_CACHE = "API_CACHE";

    @Autowired
    private CombInfService combInfService;

    @Autowired
    private ParamsService paramsService;

    @Autowired
    private EnvironmentService environmentService;

    /**
     * @Description : 组合接口获取第三方数据
     * @param request
     * @Return : java.lang.Object
     * @Date : 2019/9/16 8:55
     */
    @RequestMapping(value = "/**",produces = "application/json;text/html;charset=UTF-8")
    public Object receive(HttpServletRequest request) {
        String uid = request.getHeader(ZuulHeader.PARAM_KEY_ORDER_ID);
        try {
            String configJson = "";
            Boolean transCodeFlag = true;//转码标识
            JSONObject thirdResult = new JSONObject();
            String versionId = request.getHeader(ZuulHeader.PARAM_KEY_API_VERSION_ID);
            String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
            Integer apiId = Integer.parseInt(request.getHeader(ZuulHeader.PARAM_API_ID));
            String uniqueUuid = request.getHeader(ZuulHeader.PARAM_UNIQUE_UUID);
            String apiCache = request.getHeader(API_CACHE);
            String inmgrCode = StringUtils.isBlank(request.getHeader("inmgrCode")) ? request.getParameter("inmgrCode") : request.getHeader("inmgrCode");

            // 根据versionId 获取mainEntity
            ApiInfoResponse apiInfoResponse = paramsService.findApiInfoById(apiId);
            String jsonConfig = apiInfoResponse.getJsonConfig();

            String result = "";

            MainEntity mainEntity = paramsService.getMainEntity(versionId);
            configJson = mainEntity.getApiInfo().getResponseTransParam();
            transCodeFlag = RequestUtil.isTransCode(inmgrCode, configJson);
            MainEntity.Params params = ConvertParamsUtil.covertParams(mainEntity, request, null);
            ApiInfoVersionsWithBLOBs apiInfo = mainEntity.getApiInfo();
            String endPath = (apiInfo.getBackEndPath()==null)?"":apiInfo.getBackEndPath();
            Map<String, Object> map = null;
            if (StringUtils.isNotBlank(jsonConfig)) {
                map = objectMapper.readValue(jsonConfig, new TypeReference<Map<String, Object>>() {
                });
            }
            Object keyList = map == null ? null : map.getOrDefault(Constant.KEY_LIST, null);
            Object keyPosition = map == null ? null : map.getOrDefault(Constant.KEY_POSITION, null);
            String handleRedisParams = RedisKeyUtils.handleRedisKey(params,keyList == null ? null : keyList.toString(), keyPosition == null ? null : keyPosition.toString(), Constant.KEY_LIST_SEPERATOR);
            String redisParams = endPath + handleRedisParams;

            //清除缓存
            if (API_CACHE.equals(apiCache)) {
                logger.info("redisParams:{}", redisParams);
                String key = removeInfCache(apiInfo, redisParams, appKey);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("redisKey", key);
                return jsonObject;
            }
            Integer cacheTime = Operator.cacheTime(apiInfo.getCacheUnit(), apiInfo.getCacheNo());
            // param的md5值
            String md5 = MD5Utils.MD5(redisParams);
            String result_cache = RequestUtil.pullResultFromCacheJsonNoHtml(apiId,appKey, uniqueUuid, cacheTime, md5, null);
            logger.info("查询缓存结果{},{}", apiInfo.getApiName(), result_cache);
            if (StringUtils.isNotBlank(result_cache)) {
                result = result_cache;
            }else {
                // 外部数据源组合接口
                if (map == null)
                    map = objectMapper.readValue(jsonConfig, new TypeReference<Map<String, Object>>() {});
                map.put("uid", uid);
                if (map.containsKey("isClearInterface") && "Y".equals(map.get("isClearInterface"))) {
                    logger.info("清洗接口,header中传入uid={}", uid);
                    params.getHeaderVariables().put("uid", uid);
                    logger.info("清洗接口,param:{}", params);
                }

                JSONObject json = new JSONObject();
                Map<String, String> headerVariables = params.getHeaderVariables();
                Map<String, String> bodyVariables = params.getBodyVariables();
                Map<String, String> queryVariables = params.getQueryVariables();
                headerVariables.put("apiId", String.valueOf(apiId));
                headerVariables.put("uid",(String) map.get("uid"));
                headerVariables.put("environment",environmentService.getEnvironment());
                json.put("queryParams", queryVariables);
                json.put("bodyParams", bodyVariables);
                json.put("headerParams", headerVariables);
                Map<String, String> rspMap = combInfService.executeCombInf(json);
                result = rspMap.get("result");
                if (rspMap.containsKey("status") && "success".equals(rspMap.get("status"))) {
                    // 添加缓存
                    if (null != apiInfo.getCacheUnit())
                        RedisUtil.setResultToCache(apiId,appKey, md5, result, cacheTime, uniqueUuid, null);
                }
            }
            logger.info("第三方原始报文:{}", result);
            //转码
            ResultUtil.handleResult(thirdResult, uid, configJson, result, transCodeFlag);

            return thirdResult;
        }catch (ThirdBusinessException e) {
            logger.info("发生数据源异常: {}", e.getMessage());
            return ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_DATASOURCE_EXCEPTION.getCode())
                    .errorMsg(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.error("CombInfController error------------>", e);
            return ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode())
                    .errorMsg(e.getMessage())
                    .build();
        }

    }

    private String removeInfCache(ApiInfoVersionsWithBLOBs api, String params, String appKey) {
        logger.info("params,{}", params);
        String uniqueUuid = api.getUniqueUuid();
        Integer apiId = api.getApiId();
        String md5 = MD5Utils.MD5(params);
        String key = StringUtils.join(Constant.REDIS_KEY_RESULT, apiId, ":", appKey, ":", md5);
        logger.info("appKey,{}", appKey);
        logger.info("md5,{}", md5);
        logger.info("uniqueUuid, {}", uniqueUuid);
        JedisCluster jedis = RedisClusterUtils.getJedis();
        Long del = jedis.del(key);
        logger.info("Successfully delete redis key : {}, {}", key, del);
        return key;
    }


    private String readeRequestBody(HttpServletRequest request) {

        ServletInputStream is = null;
        try {
            is = request.getInputStream();
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[4096];
            int n;
            while ((n = is.read(b)) != -1) {
                sb.append(new String(b,0,n));
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("发生异常:{}",e.getMessage());
                }
            }
        }
    }

}
