package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.WhiteIpList;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.gateway.mapper.WhiteIpListMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: IP白名单校验规则Filter
 * @Package com.lemon.zuul.filter
 * @date 2017年9月18日 下午8:13:23
 */
public class IpWhitelistFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpWhitelistFilter.class);

    @Autowired
    private WhiteIpListMapper whiteIpListMapper;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        if (!(boolean) ctx.get("isSuccess")) {
            return null;
        }
        String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
        //IP白名单认证
        try {
            String ipv4 = (String) request.getAttribute("ipv4");
            LOGGER.info("ip地址: {}", ipv4);
            //查询redis缓存
            Object jsonObject = RedisUtil.getObject("ip:" + ipv4);
            if (jsonObject == null) {//缓存未查到，查询数据库
                int index = ipv4.lastIndexOf(".");
                String prefix = ipv4.substring(0, index + 1);
                List<WhiteIpList> ipList = whiteIpListMapper.findIpAddress(prefix);
                if (ipList != null && ipList.size() > 0) {
                    Set<String> ipStrList = new HashSet<>();
                    for (WhiteIpList ip : ipList) {
                        String ipStr = ip.getIpAddress();
                        if (!ipStr.contains("-")) {
                            ipStrList.add(ipStr);
                            continue;
                        }
                        ipStrList.addAll(getIpList(ipStr));
                    }
                    if (!ipStrList.contains(ipv4)) {
                        LOGGER.error("IP白名单校验失败");
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getCode());
                        ResponseBuilder responseBuilder = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getCode())
                                .errorMsg(ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getMessage())
                                .build();
                        ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                        ctx.set("isSuccess", false);
                        return null;
                    }
                } else {
                    LOGGER.error("IP白名单校验失败");
                    ctx.setSendZuulResponse(false);
                    request.setAttribute("errorCode", ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getCode());
                    ResponseBuilder responseBuilder = ResponseBuilder.builder()
                            .uid(orderId)
                            .errorCode(ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getCode())
                            .errorMsg(ResultStatusEnum.TFB_API_WHITELIST_NOT_FOUND.getMessage())
                            .build();
                    ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                    ctx.set("isSuccess", false);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.error("IpWhitelistFilter发生异常:{}", e.getMessage());
            ctx.setSendZuulResponse(false);
            request.setAttribute("errorCode", ResultStatusEnum.TFB_API_WHITELIST_TIMEOUT.getCode());
            ResponseBuilder responseBuilder = ResponseBuilder.builder()
                    .uid(orderId)
                    .errorCode(ResultStatusEnum.TFB_API_WHITELIST_TIMEOUT.getCode())
                    .build();
            ctx.setResponseBody(JSON.toJSONString(responseBuilder));
            ctx.set("isSuccess", false);
            return null;
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    public Set<String> getIpList(String ipAddress) {
        String[] ipList = ipAddress.split("-");
        int index = ipList[0].lastIndexOf(".");
        String prefix = ipList[0].substring(0, index + 1);
        Integer suffix = Integer.valueOf(ipList[0].substring(index + 1));
        Integer last = Integer.valueOf(ipList[1]);
        if (suffix.intValue() >= last.intValue())
            return null;
        Set<String> list = new HashSet<String>();
        for (; suffix.intValue() <= last.intValue(); suffix++) {
            list.add(prefix + suffix);
        }
        return list;
    }
}
