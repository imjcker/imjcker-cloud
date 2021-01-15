package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.http.util.NetWorkUtils;
import com.imjcker.api.common.util.UUIDUtil;
import com.imjcker.api.common.vo.*;
import com.imjcker.gateway.service.ZuulProxyService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 生成请求流水号Filter
 * @Package com.lemon.zuul.filter
 * @date 2017年7月24日 上午10:02:12
 */
public class UuidFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UuidFilter.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${inmgr.inmgrCode}")
    private String inmgrCode;
    @Autowired
    private ZuulProxyService zuulProxyService;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
        //生成全局流水号,天府银行业务系统id + 去'-'的uuid
        String uid = StringUtils.join("123010151", "-", UUIDUtil.creatUUID());

        String version = getVersion(request);
        request.setAttribute(ZuulHeader.REQUEST_VERSION, version);

        // 检测服务健康状态
        String apiHealth = request.getHeader("API_HEALTH");
        if ("API_HEALTH".equals(apiHealth)) {
            ctx.setSendZuulResponse(false);
            ctx.set("isSuccess", false);
            ResponseBuilder res = ResponseBuilder.builder()
                    .errorCode(ResultStatusEnum.CLEAN_AND_HEALTH.getCode())
                    .errorMsg(ResultStatusEnum.CLEAN_AND_HEALTH.getMessage())
                    .uid(uid)
                    .build();
            ctx.setResponseBody(JSONObject.toJSONString(res));
            return null;
        }

        //清除村镇银行自有账户　本地zuul-proxy 缓存
        String cleanZuulCache = request.getHeader(ZuulHeader.FLUSH_ZUUL_AGENCY_ACCOUNT);
        if (StringUtils.isNotBlank(cleanZuulCache)) {
            LOGGER.info("清除村镇银行账户的本地缓存,key: {}", cleanZuulCache);
            ctx.setSendZuulResponse(false);
            ctx.set("isSuccess", false);
            zuulProxyService.flushAgencyAccountCache(cleanZuulCache);
            ResponseBuilder res = ResponseBuilder.builder()
                    .errorCode(ResultStatusEnum.CLEAN_AND_HEALTH.getCode())
                    .errorMsg(ResultStatusEnum.CLEAN_AND_HEALTH.getMessage())
                    .uid(uid)
                    .build();
            ctx.setResponseBody(JSONObject.toJSONString(res));
            return null;
        }


        String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
        String realTransCode = StringUtils.isBlank(request.getHeader("inmgrCode")) ? request.getParameter("inmgrCode") : request.getHeader("inmgrCode");

        if (StringUtils.isBlank(realTransCode) || inmgrCode.equals(realTransCode)) {
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_KEY_TRANS_CODE, inmgrCode);
        } else {
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_KEY_TRANS_CODE, realTransCode);
        }
        LOGGER.info("生成的uid: {} ,请求的appKey: {},请求的inmgrCode：{}", uid, appKey, inmgrCode);
        // 添加zuul header
        ctx.addZuulRequestHeader(ZuulHeader.PARAM_UID_CREATE_TIME, String.valueOf(System.currentTimeMillis()));
        ctx.addZuulRequestHeader(ZuulHeader.PARAM_KEY_ORDER_ID, uid);
        ctx.addZuulRequestHeader(ZuulHeader.PARAM_KEY_APP_KEY, appKey);
        // 传递uid
        request.setAttribute(ZuulHeader.PARAM_KEY_ORDER_ID, uid);
        LogPojo.init(uid, LogPojo.getServerId(request), serviceName);
        MonitorUtil.putLogInit(uid, request.getRequestURL().toString(), appKey);
        try {
            String ipv4 = NetWorkUtils.getIPAddress(request);
            request.setAttribute("ipv4", ipv4);
            String querys = null;
            String bodys = null;
            if ("GET".equals(request.getMethod())) {
                querys = request.getQueryString();
            } else {
                Enumeration<String> parameterNames = request.getParameterNames();
                JSONObject params = new JSONObject();
                while (parameterNames.hasMoreElements()) {
                    String key = parameterNames.nextElement();
                    String value = request.getParameter(key);
                    params.put(key, value);
                }
                bodys = params.toJSONString();
            }
            LOGGER.debug("请求信息:\r\nipAddress: {}\r\nProtocol: {}\r\nMethod: {}\r\nUrl: {}\r\nQuerys: {}\r\nBodys: {}",
                    ipv4,
                    request.getProtocol(),
                    request.getMethod(),
                    request.getRequestURL(),
                    querys,
                    bodys);
            MonitorUtil.putIpv4(ipv4);
        } catch (Exception e) {
            ResponseBuilder response = ResponseBuilder.builder()
                    .uid(uid)
                    .errorCode(ResultStatusEnum.TFB_API_IP_ERROR.getCode())
                    .errorMsg(ResultStatusEnum.TFB_API_IP_ERROR.getMessage())
                    .build();
            MonitorUtil.putIpv4("获取请求真实ip出错");
            ctx.setSendZuulResponse(false);
            request.setAttribute("errorCode", ResultStatusEnum.TFB_API_IP_ERROR.getCode());
            ctx.setResponseBody(JSON.toJSONString(response));
            ctx.set("isSuccess", false);
            return null;
        }
        ctx.set("isSuccess", true);
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
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
            version = Constant.REQUEST_VERSION_OLD;
        }
        return version;
    }
}
