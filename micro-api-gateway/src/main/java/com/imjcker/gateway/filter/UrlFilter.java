package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.CollectionUtil;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.api.common.vo.*;
import com.imjcker.gateway.model.ApiInfoRedisMsg;
import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.service.ZuulProxyService;
import com.imjcker.gateway.util.InvokeUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: URL合法性校验Filter
 * @Package com.lemon.zuul.filter
 * @date 2017年7月24日 上午10:02:51
 */
public class UrlFilter extends ZuulFilter {

    @Autowired
    private ZuulProxyService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlFilter.class);

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {

            if (!(boolean) ctx.get("isSuccess")) {
                return null;
            }
            String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
            ctx.getResponse().setCharacterEncoding("UTF-8");
            //httpPath合法性验证
            String contextPath = request.getContextPath();
            String httpPath = request.getRequestURI().replaceFirst(contextPath, "");
            // 由于风控，对公业务内部服务调用没有传递接口httpPath，故通过接口id来查询到httpPath.
            if (InvokeUtil.isInternalInvoke(request)) {
                Integer apiId = Integer.valueOf(request.getHeader("apiId"));
                JSONObject jsonObject = RedisUtil.get("api:" + apiId);
                if (jsonObject != null) {
                    ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
                    httpPath = apiInfoRedisMsg.getApiInfoWithBLOBs().getHttpPath();
                } else {
                    httpPath = service.getHttpPathByApiId(apiId);
                }
                LOGGER.debug("内部服务调用构造的apiId:{}, httpPath:{}", apiId, httpPath);
                if (httpPath == null) {
                    ResponseBuilder responseBuilder = ResponseBuilder.builder()
                            .errorCode(ResultStatusEnum.TFB_API_HTTPPATH_NOT_FOUND.getCode())
                            .errorMsg("httpPath is null")
                            .uid(orderId)
                            .build();
                    ctx.setSendZuulResponse(false);
                    request.setAttribute("errorCode", ResultStatusEnum.TFB_API_HTTPPATH_NOT_FOUND.getCode());
                    ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                    ctx.set("isSuccess", false);
                    return null;
                }
            }
            //根据接口httpPath和env查询接口信息，先从redis中查询，未查到再查询数据库
            Integer env = Operator.getEnv(request.getHeader("env"));
            LOGGER.debug("httpPath: {} ,env :{}", httpPath, env);
            String redisKey = new StringBuilder("api:env").append(env).append(":httpPath:").append(httpPath).toString();
            ApiInfoVersions entity;
            JSONObject jsonObject = RedisUtil.get(redisKey);
            if (jsonObject != null) {
                LOGGER.debug("redis中存在api:env{}:httpPath={}", env, httpPath);
                entity = jsonObject.toJavaObject(ApiInfoVersions.class);
            } else {
                LOGGER.debug("redis中不存在api:env{}:httpPath={},访问数据库", env, httpPath);
                List<ApiInfoVersions> list = service.getCurrentApiInfo(httpPath, env);
                if (CollectionUtil.isEmpty(list)) {
                    ResponseBuilder responseBuilder = ResponseBuilder.builder()
                            .errorCode(ResultStatusEnum.TFB_API_HTTPPATH_NOT_FOUND.getCode())
                            .errorMsg(ResultStatusEnum.TFB_API_HTTPPATH_NOT_FOUND.getMessage())
                            .uid(orderId)
                            .build();
                    ctx.setSendZuulResponse(false);
                    request.setAttribute("errorCode", ResultStatusEnum.TFB_API_HTTPPATH_NOT_FOUND.getCode());
                    ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                    ctx.set("isSuccess", false);
                    return null;
                }
                entity = list.get(0);
                RedisUtil.setToCaches(redisKey, entity);
            }
            //httpMethod合法性验证
            String httpMethod = request.getMethod();
            String httpMethodFlag = entity.getHttpMethod() == 1 ? "GET" : "POST";
            if (!httpMethod.equals(httpMethodFlag)) {
                LOGGER.error("HTTP Method 校验不合法!");
                ResponseBuilder responseBuilder = ResponseBuilder.builder()
                        .errorCode(ResultStatusEnum.TFB_API_HTTPMETHOD_NOT_SUPPORT.getCode())
                        .errorMsg(ResultStatusEnum.TFB_API_HTTPMETHOD_NOT_SUPPORT.getMessage())
                        .uid(orderId)
                        .build();
                ctx.setSendZuulResponse(false);
                request.setAttribute("errorCode", ResultStatusEnum.TFB_API_HTTPMETHOD_NOT_SUPPORT.getCode());
                ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                ctx.set("isSuccess", false);
                return null;
            }
            //apiId存入request下一级判断授权
            request.setAttribute("apiId", entity.getApiId());
            //apiGroupId存入request下一级判断授权
            request.setAttribute("groupId", entity.getApiGroupId());
            //jsonConfig存入request下一级判断授权
            request.setAttribute("jsonConfig", entity.getJsonConfig());
            //api的限流策略
            request.setAttribute("api_limit_uuid", entity.getLimitStrategyuuid());
            request.setAttribute("api_limit_total", entity.getLimitStrategyTotal() == null ? -1 : entity.getLimitStrategyTotal());
            //是否走计费规则
            request.setAttribute("is_charge", entity.getCharge());
            //VersionId存入header，供调用第三方时使用
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_KEY_API_VERSION_ID, entity.getVersionId());
            request.setAttribute(ZuulHeader.PARAM_KEY_API_VERSION_ID, entity.getVersionId());
            //apiId存入header，供调用第三方时使用
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_API_ID, entity.getApiId().toString());
            //uniqueUuid存入header，供调用第三方时使用
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_UNIQUE_UUID, entity.getUniqueUuid());
            //weight存入header，供调用第三方时使用
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_WEIGTH, entity.getWeight().toString());
            if (entity.getIsMock() == 1) {
                request.setAttribute("mock_data", entity.getMockData());
            }
            //暂存mongo日志信息
            MonitorUtil.putLogApiIdAndVersionId(entity.getApiId(), entity.getVersionId(), entity.getApiName());
            MonitorMsgPoJo s = MonitorUtil.getLog();
            //存普通日志
            LogPojo logPojo = LogPojo.getLog();
            logPojo.setApiVersionId(entity.getVersionId());
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}
