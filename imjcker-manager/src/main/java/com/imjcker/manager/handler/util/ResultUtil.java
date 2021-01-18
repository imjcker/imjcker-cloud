package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * .
 * User: lxl
 * Date: 2017/10/17
 * Time: 13:14
 * Description:
 */
public class ResultUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ResultUtil.class);

    private static String ORIGN_KEY = "key";
    private static String TARGET_KEY = "transKey";
    private static String TRANS_OPTION_KEY = "option";
    private static String TRANS_OPTION_ORIGN_VALUE_KEY = "orign";
    private static String TRANS_OPTION_TARGET_VALUE_KEY = "target";
    private static String LEVEL_KEY = "deep";
    private static String IS_DATA_KEY = "isData";
    /**
     * 定义报文返回的基础key值
     */
    private static String EXTRACT_BASE_DATA_KEY = "data";
    private static String EXTRACT_BASE_CODE_KEY = "errorCode";
    private static String EXTRACT_BASE_MSG_KEY = "errorMsg";

    /**
     * 获取object需要的json类型,只针对jsonobject.get的object
     *
     * @param object 需要判断的json对象
     * @return object匹配的类型
     */
    public static JsonTypeEnum getType(Object object) {
        if (object instanceof String) {
            return JsonTypeEnum.STRING;
        } else if (StringUtils.isNumeric(object.toString())) {
            return JsonTypeEnum.NUMBER;
        } else if (object instanceof JSONObject) {
            return JsonTypeEnum.JSON_OBJECT;
        } else if (object instanceof JSONArray) {
            return JsonTypeEnum.JSON_ARRAY;
        } else {
            return JsonTypeEnum.OTHER;
        }
    }

    public static void handleResult(JSONObject thirdResult, String orderId, String configStr, String resultJson, Boolean transCodeFlag) {

        try {

            JSONObject resultObject = JSON.parseObject(resultJson);

            //请求发生异常或失败直接返回异常信息
            if (resultObject.containsKey("exceptionDesc") && resultObject.containsKey("errorCode") && resultObject.getInteger("errorCode") == 500) {
                thirdResult.put("uid", orderId);
                resultObject.forEach(thirdResult::put);
                return;
            }
//            if (StringUtils.isBlank(configStr)) {//不转码
            if (!transCodeFlag) {//不转码
                for (Map.Entry<String, Object> entry : resultObject.entrySet()) {
                    String key = entry.getKey();
                    thirdResult.put(key, resultObject.get(key));
                }
            } else {//转码
                JSONArray configJsons = JSON.parseArray(configStr);
                Map<String, Object> extractStructure = new HashMap<String, Object>();
                for (Object config : configJsons) {
                    JSONObject configJson = (JSONObject) config;
                    analysisJson(resultObject, 0, configJson, thirdResult, extractStructure);
                }
                thirdResult.put(EXTRACT_BASE_DATA_KEY, new JSONObject());
                extractStructure(thirdResult, extractStructure);
            }
            thirdResult.put("uid", orderId);
        } catch (Exception e) {
            LOGGER.debug(transCodeFlag ? "转码" : "不转码");
            LOGGER.error("transfer json result failed, error info {}", e);
            thirdResult.put("errorMsg", JsonResult.StatusCode.error.getDesc());
            thirdResult.put("errorCode", JsonResult.StatusCode.error.getCode());
        }
    }


    /**
     * 按照层级转换json的key值以及对value值进行转码
     *
     * @param resultJson  上游接口返回的json
     * @param level       默认从0开始
     * @param config      字段的转换的配置
     * @param thirdResult 统一接口
     * @return extractStructure 抽取的结构
     * 返回接口
     */
    private static void analysisJson(Object resultJson, int level, JSONObject config, JSONObject thirdResult, Map<String, Object> extractStructure) {
        //如果obj为json数组
        if (ResultUtil.getType(resultJson).equals(JsonTypeEnum.JSON_ARRAY)) {
            JSONArray objArray = (JSONArray) resultJson;
            for (int i = 0; i < objArray.size(); i++) {
                //如果数组的下级为为jsonObject则层级为当前数组的下级
                analysisJson(objArray.get(i), (ResultUtil.getType(objArray.get(i)).equals(JsonTypeEnum.JSON_OBJECT)) ?
                        level : level + 1, config, thirdResult, extractStructure);
            }
        } else if (ResultUtil.getType(resultJson).equals(JsonTypeEnum.JSON_OBJECT)) {
            JSONObject jsonObject = (JSONObject) resultJson;
            //JSONObject 结构是map结构 循环完毕统一去除原有key值

            Set<String> orignKey = new HashSet<String>();
            Map<String, Object> transNode = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                Object value = jsonObject.get(key);
                if (value != null) {
                    switch (ResultUtil.getType(value)) {
                        case JSON_ARRAY:
                            JSONArray objArray = (JSONArray) value;
                            analysisJson(objArray, level + 1, config, thirdResult, extractStructure);
                            break;
                        case JSON_OBJECT:
                            analysisJson(value, level + 1, config, thirdResult, extractStructure);
                            break;
                        default:
                            if (key.equals(config.getString(ORIGN_KEY))
                                    && level == config.getIntValue(LEVEL_KEY)
                                    && config.get(TRANS_OPTION_KEY) != null) {
                                for (Object optionObj : config.getJSONArray(TRANS_OPTION_KEY)) {
                                    JSONObject option = (JSONObject) optionObj;
                                    if (value.toString().equals(option.getString(TRANS_OPTION_ORIGN_VALUE_KEY))) {
                                        if (config.getString(TARGET_KEY).equals(EXTRACT_BASE_CODE_KEY)) {
                                            //如果转换的key值为EXTRACT_BASE_CODE_KEY进行值的转码
                                            String codeData = (String) option.get(TRANS_OPTION_TARGET_VALUE_KEY);
                                            thirdResult.put(EXTRACT_BASE_CODE_KEY, (Integer.valueOf(StringUtils.substringBeforeLast(codeData, ","))));
                                        } else if (config.getString(TARGET_KEY).equals(EXTRACT_BASE_MSG_KEY)) {
                                            thirdResult.put(EXTRACT_BASE_MSG_KEY, StringUtils.substringBeforeLast(option.getString(TRANS_OPTION_TARGET_VALUE_KEY), ","));
                                        } else {
                                            entry.setValue(StringUtils.substringBeforeLast(option.getString(TRANS_OPTION_TARGET_VALUE_KEY), ","));
                                        }

                                    }
                                }

                            }
                            break;
                    }
                }

                //缓存替换的节点
                if (key.equals(config.getString(ORIGN_KEY)) && level == config.getIntValue(LEVEL_KEY) && StringUtils.isNotBlank(config.getString(TARGET_KEY))) {
                    if (!(config.getString(TARGET_KEY).equals(EXTRACT_BASE_CODE_KEY) || config.getString(TARGET_KEY).equals(EXTRACT_BASE_MSG_KEY))) {
                        //如果target的key值为errorCode不进行转换
                        orignKey.add(config.getString(ORIGN_KEY));
                        transNode.put(config.getString(TARGET_KEY), entry.getValue());
                    }
                }

                if (key.equals(config.getString(ORIGN_KEY)) && level == config.getIntValue(LEVEL_KEY)) {
                    if (config.getString(IS_DATA_KEY) != null && Boolean.parseBoolean(config.getString(IS_DATA_KEY)) && !(config.getString(TARGET_KEY).equals(EXTRACT_BASE_CODE_KEY) || config.getString(TARGET_KEY).equals(EXTRACT_BASE_MSG_KEY))) {
                        extractStructure.put(StringUtils.isBlank(config.getString(TARGET_KEY)) ? config.getString(ORIGN_KEY) : config.getString(TARGET_KEY), entry.getValue());
                    }
                }
            }
            //加入替换的key
            for (Map.Entry<String, Object> entry : transNode.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());

            }
            //去除原有的key值
            for (String orign : orignKey) {
                jsonObject.remove(orign);
            }
        }
    }


    private static void extractStructure(JSONObject thirdResult, Map<String, Object> extractStructure) {
        JSONObject data = (JSONObject) thirdResult.get(EXTRACT_BASE_DATA_KEY);
        for (Map.Entry<String, Object> entry : extractStructure.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
        thirdResult.put(EXTRACT_BASE_DATA_KEY, data);
    }
}
