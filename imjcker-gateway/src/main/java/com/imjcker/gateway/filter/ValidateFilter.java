package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.MonitorUtil;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.gateway.po.RequestParamsVersions;
import com.imjcker.gateway.service.ZuulProxyService;
import com.imjcker.gateway.util.TypeValidateUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class ValidateFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateFilter.class);

    @Autowired
    private ZuulProxyService service;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        if (!(boolean) ctx.get("isSuccess")) {
            return null;
        }
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }

        String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
        String versionId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_API_VERSION_ID);
        if (versionId == null) return null;
        ctx.getResponse().setCharacterEncoding("UTF-8");
        String version = (String) request.getAttribute(ZuulHeader.REQUEST_VERSION);
        JSONObject paramJson = new JSONObject();
        String paramData = null;
        if (com.imjcker.api.common.vo.Constant.REQUEST_VERSION_OLD.equals(version)) {
            //v1表示老数据流程
            Enumeration enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                paramJson.put(paraName, request.getParameter(paraName));
            }
            paramData = JSON.toJSONString(paramJson);
        } else {
            paramJson = JSON.parseObject(paramData);
        }
        MonitorUtil.putLogParams(paramJson);

        List<RequestParamsVersions> list = service.getEntitybyVersionId(versionId);
        //根据数据库保存的参数名称与参数位置来取请求信息中的参数
        for (RequestParamsVersions para : list) {
            Integer type = para.getParamsLocation();
            String name = para.getParamName();
            String getPara = null;
            //从不同方式得到参数
            if (1 == type) {
                getPara = request.getHeader(name);
            } else if (3 == type) {
                getPara = paramJson.getString(name);

            } else {
                getPara = paramJson.getString(name);
            }
            if (StringUtils.isBlank(request.getHeader("third")) && (com.imjcker.gateway.util.Constant.PARAM_IS_MUST == para.getParamsMust() && StringUtils.isBlank(getPara))) {
                //是否必填校验
                String data = StringUtils.join("参数：", name, " 为必填项");
                ResponseBuilder responseBuilder = ResponseBuilder.builder()
                        .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                        .errorMsg(data)
                        .uid(orderId)
                        .build();
                ctx.setSendZuulResponse(false);
                request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                return null;
            }
            //数据类型校验
            if (StringUtils.isNotBlank(getPara)) {
                Integer paramsType = para.getParamsType();
                if (paramsType == com.imjcker.gateway.util.Constant.STRING_TYPE) {
                    if (para.getMinLength() != null && getPara.length() < para.getMinLength()) {
                        String data = StringUtils.join("参数：", name, " 长度错误，", "长度须为：", para.getMinLength(), "-", para.getMaxLength());
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                    if (para.getMaxLength() != null && getPara.length() > para.getMaxLength()) {
                        String data = StringUtils.join("参数：", name, " 长度错误，", "长度须为：", para.getMinLength(), "-", para.getMaxLength());
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                    //正则表达式校验
                    String pattern = para.getRegularExpress();
                    if (StringUtils.isNotBlank(pattern)) {
                        if (!Pattern.matches(pattern, getPara)) {
                            String data = StringUtils.join("参数：", name, "，值：", getPara, " 校验失败");
                            ResponseBuilder response = ResponseBuilder.builder()
                                    .uid(orderId)
                                    .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                    .errorMsg(data)
                                    .build();
                            ctx.setSendZuulResponse(false);
                            request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                            ctx.setResponseBody(JSON.toJSONString(response));
                            return null;
                        }
                    }
                } else if (paramsType == com.imjcker.gateway.util.Constant.INT_TYPE) {
                    if (!TypeValidateUtil.isInt(getPara)) {
                        String data = StringUtils.join("参数：", name, "，值：", getPara, "，不是有效的int型数据");
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                } else if (paramsType == com.imjcker.gateway.util.Constant.LONG_TYPE) {
                    if (!TypeValidateUtil.isLong(getPara)) {
                        String data = StringUtils.join("参数：", name, "，值：", getPara, "，不是有效的long型数据");
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                } else if (paramsType == com.imjcker.gateway.util.Constant.FLOAT_TYPE) {
                    if (!TypeValidateUtil.isFloat(getPara)) {
                        String data = StringUtils.join("参数：", name, "，值：", getPara, "，不是有效的float型数据");
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                } else if (paramsType == com.imjcker.gateway.util.Constant.DOUBLE_TYPE) {
                    if (!TypeValidateUtil.isDoublle(getPara)) {
                        String data = StringUtils.join("参数：", name, "，值：", getPara, "，不是有效的double型数据");
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                } else if (paramsType == com.imjcker.gateway.util.Constant.BOOLEAN_TYPE) {
                    if (!TypeValidateUtil.isBoolean(getPara)) {
                        String data = StringUtils.join("参数：", name, "，值：", getPara, "，不是有效的boolean型数据");
                        ResponseBuilder response = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode())
                                .errorMsg(data)
                                .build();
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_API_PARAMETERS_ERROR.getCode());
                        ctx.setResponseBody(JSON.toJSONString(response));
                        return null;
                    }
                }
            }
        }

        //Mock数据返回
        String mockData = (String) request.getAttribute("mock_data");
        if (StringUtils.isNotBlank(mockData)) {
            ctx.setSendZuulResponse(false);
            request.setAttribute("errorCode", ResultStatusEnum.TFB_SUCCESS.getCode());
            ctx.setResponseBody(mockData);
            return null;
        }
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
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

}
