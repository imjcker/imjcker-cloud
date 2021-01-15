package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.MainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.BiFunction;

public class RedisKeyUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisKeyUtils.class);

    public RedisKeyUtils() {

    }

    private static BiFunction<JSONObject, String, JSONObject> method = JSONObject::getJSONObject;

    /**
     * 取得下层JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    private static JSONObject getSubKey(JSONObject jsonObject, String key) {
        return method.apply(jsonObject, key);
    }

    /**
     * 直接从参数主体中处理不需要参与加密的key
     * 直接将Params对象转成JSON耗时很长...
     *
     * @param params   请求参数
     * @param position 主体参数所在位置
     * @return 有可能是String或者Map或者MainEntity.Params，这里直接返回Object
     */
    private static Object getParam(MainEntity.Params params, String position) {
        Object param;
        switch (position) {
            case "header":
                param = params.getHeaderVariables();
                break;
            case "query":
                param = params.getQueryVariables();
                break;
            case "body":
                param = params.getBodyVariables();
                break;
            case "json":
                param = params.getJson();
                break;
            case "xml":
                param = params.getXml();
                break;
            default:
                param = params;
                break;
        }
        return param;
    }

    /**
     * 处理redis key方法
     *
     * @param params    入参
     * @param keyList   不需要参与加密的key
     * @param position  参数主体所在位置
     * @param separator 分隔符(多个参数','分隔，层级'.'分隔)
     * @return 参与加密的参数
     */
    public static String handleRedisKey(MainEntity.Params params, String keyList, String position, String separator) throws Exception {
        //没有配置处理key的直接返回全部参数
        if (keyList == null || position == null)
            return params.toString();

        String[] keys = keyList.split(separator);
        Object param = getParam(params, position);

        //xml单独处理转成JSON
        if ("xml".equalsIgnoreCase(position) || param instanceof String)
            param = ExtractUtil.xml2Json(param.toString());
        else
            param = JSONObject.toJSON(param);

        //进行key移除
        JSONObject paramJson = (JSONObject) param;
        Arrays.stream(keys).forEach(key -> {
            String[] subKeyList = key.split(Constant.SUBKEY_SEPERATOR);
            JSONObject fatherNode = paramJson;
            int subKeyLength = subKeyList.length;
            if (subKeyLength > 1) {
                JSONObject childNode;
                for (int i = 0; i < subKeyLength - 1; i++) {
                    childNode = getSubKey(fatherNode, subKeyList[i]);
                    fatherNode.fluentPut(subKeyList[i], childNode);
                    if (i == subKeyLength - 2) {
                        childNode.fluentRemove(subKeyList[subKeyLength - 1]);
                        logger.info("已移除key:{}", subKeyList[subKeyLength - 1]);
                        break;
                    }
                    fatherNode = childNode;
                }
            } else {
                fatherNode.fluentRemove(key);
            }
        });
        return paramJson.toJSONString();
    }
}
