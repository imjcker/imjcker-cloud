package com.imjcker.api.handler.util;

import com.imjcker.api.common.util.Operator;
import com.imjcker.api.handler.model.ChildEntity;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.BackendRequestParamsVersions;
import com.imjcker.api.handler.po.GrandEntityBackParam;
import com.imjcker.api.handler.po.RequestParamsVersions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WT
 * @Date 9:52 2019/9/23
 * @Version ConvertParamsUtil v1.0
 * @Desicrption
 */
public class ConvertParamsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertParamsUtil.class);

    public static MainEntity.Params covertParams(MainEntity mainEntity, HttpServletRequest request,
                                                 Map<String, Object> accountMap) {
        Map<String, String> headerVariables = new HashMap<String, String>(); // header中的参数
        Map<String, String> queryVariables = new LinkedHashMap<String, String>(); // query的参数,get方式有
        Map<String, String> bodyVariables = new LinkedHashMap<String, String>(); // body中的参数,post方式有
        List<BackendRequestParamsVersions> backParams = mainEntity.getBackParams();
        return (MainEntity.Params) convertParamsAbs(backParams, request, headerVariables, queryVariables,
                bodyVariables, mainEntity.getRequestParams(), true, accountMap);
    }

    private static Object convertParamsAbs(List<?> backParams, HttpServletRequest request,
                                           Map<String, String> headerVariables, Map<String, String> queryVariables,
                                           Map<String, String> bodyVariables, List<RequestParamsVersions> requestParams,
                                           boolean isFather, Map<String, Object> accountMap) {

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
                            getValueXMLandJson(request, entity, requestParams));
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
                            getValueXMLandJson(request, entity, requestParams));
                }
                if (isFather) {
                    if (xml != null) {
                        xml = replaceDateExpression(xml);
                    } else {
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
                    putValue2MapWithLocation(headerVariables, bodyVariables, queryVariables, location,
                            entity.getParamName(), getValue(request, entity, requestParams));
                }
            } else {
                LOGGER.debug("替换常量值参数");
                for (Object object : backParams) {
                    entity = (GrandEntityBackParam) object;
                    Integer location = entity.getParamsLocation();
                    putValue2MapWithLocation(headerVariables, bodyVariables, queryVariables, location,
                            entity.getParamName(), getChangeValue(request, entity, requestParams, accountMap));
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
        } else {
            return new ChildEntity.Params(headerVariables, queryVariables, bodyVariables, json, xml);
        }
    }

    private static String getChangeValue(HttpServletRequest request, GrandEntityBackParam back,
                                         List<RequestParamsVersions> requestParams, Map<String, Object> map) {
        String value = back.getParamValue();
        // 常量参数
        if (ConstantEnum.ParamsType.constant.getValue() == back.getParamsType()) {
            String paramName = back.getParamName();
            if (map.containsKey(paramName)) {
                return (String) map.get(paramName);
            }
            return value;
        }
        return getValueXMLandJson(request, back, requestParams);
    }

    private static void putValue2MapWithLocation(Map<String, String> headerVariables, Map<String, String> bodyVariables,
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

    private static String getValue(HttpServletRequest request, GrandEntityBackParam back, List<RequestParamsVersions> requestParams) {
        String value = back.getParamValue();
        // 常量参数
        if (ConstantEnum.ParamsType.constant.getValue() == back.getParamsType()) {
            return value;
        }
        return getValueXMLandJson(request, back, requestParams);
    }

    private static String replaceDateExpression(String encodeValue) {
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

    private static String replaceTimePattern(String encodeValue) {
        if (encodeValue.indexOf("${timeReplacePattern}") > -1) {
            int start = encodeValue.indexOf("@") + 1;
            int end = encodeValue.lastIndexOf("@");
            String pattern = encodeValue.substring(start, end);
            encodeValue = Operator.replace(encodeValue, "timeReplacePattern", DateUtil.getTodayWithPattern(pattern));
            encodeValue = Operator.replace(encodeValue, "@" + pattern + "@", "");
        }
        return encodeValue;
    }

    private static String getValueXMLandJson(HttpServletRequest request, GrandEntityBackParam grand, List<RequestParamsVersions> requestParams) {

        Integer requestParamsId = grand.getRequestParamsId();
        RequestParamsVersions frontPara = null;
        for (RequestParamsVersions reqParam : requestParams) {
            if (reqParam.getRequestParamsId().equals(requestParamsId)) {
                frontPara = reqParam;
                break;
            }
        }
        if (frontPara == null)
            return null; // 没有查询到id对应的RequestParamsVersions,直接返回null

        String value = getRequestValue(request, frontPara); // 取传入的参数
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            String defaultValue = frontPara.getParamsDefaultValue();
            return StringUtils.isBlank(defaultValue) ? "" : defaultValue;// 取默认值
        }
    }

    private static String getRequestValue(HttpServletRequest request, RequestParamsVersions frontPara) {
        Integer location = frontPara.getParamsLocation();
        String name = frontPara.getParamName();
        if (ConstantEnum.ParamsLocation.head.getValue() == location) {
            return request.getHeader(name);
        } else if (ConstantEnum.ParamsLocation.body.getValue() == location) {
            return request.getParameter(name);
        } else {
            return request.getParameter(name);
        }
    }
}
