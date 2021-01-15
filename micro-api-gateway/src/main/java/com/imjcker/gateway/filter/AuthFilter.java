package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.MonitorUtil;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.gateway.po.CompanyAppsAuth;
import com.imjcker.gateway.service.ZuulProxyService;
import com.imjcker.gateway.service.impl.AuthManager;
import com.imjcker.gateway.util.AppkeyAuthEntity;
import com.imjcker.gateway.util.ErrorResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author thh
 */
public class AuthFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private ZuulProxyService service;

    @Autowired
    private AuthManager authManager;

    @Value("#{'${agency.api.appKey}'.split(',')}")
    private List<String> appKeyList;

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        if (!(boolean) ctx.get("isSuccess")) {
            return null;
        }
        ctx.getResponse().setCharacterEncoding("UTF-8");

        String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
        Integer apiId = (Integer) request.getAttribute("apiId");
        String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
        LOGGER.debug("appKey: {} ,apiId: {}", appKey, apiId);
        //判断Appkey是否合法
        //appKey权限验证
        try {
            AppkeyAuthEntity entity = new AppkeyAuthEntity();
            entity.setAppKey(appKey);
            entity.setApiId(apiId);
            entity.setUid(orderId);
            String result = authManager.appKeyAuth(entity);
            LOGGER.info("AppKey校验结果:{}", result);
            ErrorResult resultObject = JSONObject.parseObject(result, ErrorResult.class);

            if (resultObject.getErrorCode() != 200) {
                ctx.setSendZuulResponse(false);
                request.setAttribute("errorCode", ResultStatusEnum.TFB_API_APPKEY_AUTH_FAILED.getCode());
                ResponseBuilder responseBuilder = ResponseBuilder.builder()
                        .uid(orderId)
                        .errorCode(ResultStatusEnum.TFB_API_APPKEY_AUTH_FAILED.getCode())
                        .errorMsg(resultObject.getErrorMsg())
                        .build();
                //ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                ctx.set("isSuccess", false);
                return null;
            }
            //appKey验证通过标志
            request.setAttribute("isRightKey", "YES");
            //合约验证通过，存入合约绑定规则和单价字段到ES
            CompanyAppsAuth companyAppsAuth = resultObject.getCompanyAppsAuth();//合约信息
            MonitorUtil.putLogBillingRulesUuidAndPrice(companyAppsAuth.getStrategyUuid(), companyAppsAuth.getPrice().doubleValue());
            //appkey限流策略存入请求供限流器使用
//            request.setAttribute("app_limit_strategy", jsonObject2.get("strategy") == null ? null : jsonObject2.get("strategy").toString());
        } catch (Exception e) {
            LOGGER.error("AuthFilter发生异常: {}", e);
            ctx.setSendZuulResponse(false);
            request.setAttribute("errorCode", ResultStatusEnum.TFB_API_APPKEY_TIMEOUT.getCode());
            ResponseBuilder responseBuilder = ResponseBuilder.builder()
                    .uid(orderId)
                    .errorCode(ResultStatusEnum.TFB_API_APPKEY_TIMEOUT.getCode())
                    .errorMsg(e.getMessage())
                    .build();
            ctx.setResponseBody(JSON.toJSONString(responseBuilder));
            ctx.set("isSuccess", false);
            return null;
        }

        // 获取村镇银行自有的第三方数据源账户
        if (appKeyList.contains(appKey) && StringUtils.isNotBlank((String) request.getAttribute("jsonConfig"))) {
            LOGGER.info("更换机构账户...");
            Integer groupId = (Integer) request.getAttribute("groupId");

            String dataConfig = service.getDataConfigWithTownBank(appKey, groupId, apiId);
            if (StringUtils.isBlank(dataConfig)) {
                //返回异常码
                ResponseBuilder responseBuilder = ResponseBuilder.builder()
                        .errorCode(ResultStatusEnum.TOWN_ACCOUNT_NOT_FOUND.getCode())
                        .errorMsg(ResultStatusEnum.TOWN_ACCOUNT_NOT_FOUND.getMessage())
                        .uid(orderId)
                        .build();
                ctx.setSendZuulResponse(false);
                request.setAttribute("errorCode", ResultStatusEnum.TOWN_ACCOUNT_NOT_FOUND.getCode());
                ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                ctx.set("isSuccess", false);
                return null;
            }
            ctx.addZuulRequestHeader(ZuulHeader.PARAM_AGENCY_API_ACCOUNT, JSONObject.parseObject(dataConfig).toJSONString());
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
}
