//package com.imjcker.gateway.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.imjcker.api.common.vo.ResponseBuilder;
//import com.imjcker.api.common.vo.ResultStatusEnum;
//import com.imjcker.api.common.vo.ZuulHeader;
//import com.imjcker.gateway.service.ChargeService;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @Author WT
// * @Date 9:36 2020/3/11
// * @Version ChargeFilter v1.0
// * @Desicrption 余额校验, 计费
// */
//public class ChargeFilter extends ZuulFilter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeFilter.class);
//
//    @Autowired
//    private ChargeService chargeService;
//
//    @Override
//    public Object run() {
//
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//
//        if (!(boolean) ctx.get("isSuccess")) {
//            return null;
//        }
//        // 如果是清除缓存,无需计费
//        String apiCache = request.getHeader("API_CACHE");
//        if ("API_CACHE".equals(apiCache))
//            return null;
//        ctx.getResponse().setCharacterEncoding("UTF-8");
//
//        String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");
//        Integer apiId = (Integer) request.getAttribute("apiId");
//        String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
//        try {
//            if (!chargeService.chargePrice(appKey, apiId)) {
//                // 余额不足
//                LOGGER.error("余额不足,appKey: {} ,apiId: {}", appKey, apiId);
//                ctx.setSendZuulResponse(false);
//                request.setAttribute("errorCode", ResultStatusEnum.TFB_API_BALANCE_ERROR.getCode());
//                ResponseBuilder responseBuilder = ResponseBuilder.builder()
//                        .uid(orderId)
//                        .errorCode(ResultStatusEnum.TFB_API_BALANCE_ERROR.getCode())
//                        .errorMsg(ResultStatusEnum.TFB_API_BALANCE_ERROR.getMessage())
//                        .build();
//                ctx.setResponseBody(JSON.toJSONString(responseBuilder));
//                ctx.set("isSuccess", false);
//                return null;
//            }
//            // 添加计费成功标识
//            request.setAttribute("isCharge", "YES");
//        } catch (Exception e) {
//            LOGGER.error("ChargeFilter发生异常: {}", e.getMessage());
//            ctx.setSendZuulResponse(false);
//            request.setAttribute("errorCode", ResultStatusEnum.TFB_API_APPKEY_TIMEOUT.getCode());
//            ResponseBuilder responseBuilder = ResponseBuilder.builder()
//                    .uid(orderId)
//                    .errorCode(ResultStatusEnum.TFB_API_APPKEY_TIMEOUT.getCode())
//                    .errorMsg(e.getMessage())
//                    .build();
//            ctx.setResponseBody(JSON.toJSONString(responseBuilder));
//            ctx.set("isSuccess", false);
//            return null;
//        }
//        return null;
//    }
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 6;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//}
