package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.api.handler.po.RequestParamAndValue;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {

    }

    public static String prettyJson(Object object) throws JsonProcessingException {

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static String getJson(List<RequestParamAndValue> list) {
        JSONObject jsonObject = new JSONObject();
        for (RequestParamAndValue item : list) {
            if (StringUtils.isNotBlank(item.getParamValue())) {
                jsonObject.put(item.getParamName(), item.getParamValue());
            }
        }
        String jsonString = jsonObject.toJSONString();
        return jsonString;
    }
}
