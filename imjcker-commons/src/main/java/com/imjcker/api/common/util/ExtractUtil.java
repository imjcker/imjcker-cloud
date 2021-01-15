package com.imjcker.api.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.XML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年9月4日 下午2:46:01
 * @Title 抽取XML/JSON指定数据封装类
 * @Description
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
public class ExtractUtil {
    private ExtractUtil() {
        throw new RuntimeException("禁止实例化XML/JSON取值工具类");
    }

    /**
     * 检查json类型
     *
     * @param json json对象
     * @return 1：jsonObject  2：jsonArray  0:值value
     */
    private static int checkType(String json) {
        try {
            JSONObject.parseObject(json);
            return 1;
        } catch (JSONException e) {
            try {
                JSONObject.parseArray(json);
                return 2;
            } catch (JSONException e1) {
                return 3;
            }
        }
    }

    /**
     * 获取查找值对应Value
     *
     * @param xmlJSONObj json对象
     * @param goalKey    查找值
     * @return
     */
    private static String getGoalValue(JSONObject xmlJSONObj, String goalKey) {
        JSONObject json = xmlJSONObj;
        String goalValue = null;
        if (json.containsKey(goalKey)) {
            return json.get(goalKey).toString();
        } else {
            Iterator ite = json.keySet().iterator();
            int i;
            List<String> list = new ArrayList<String>();
            while (ite.hasNext()) {
                String k = (String) ite.next();

                i = checkType(json.get(k).toString());
                if (1 == i) {
                    JSONObject jsonObj = json.getJSONObject(k);
                    goalValue = getGoalValue(jsonObj, goalKey);
                } else if (2 == i) {
                    com.alibaba.fastjson.JSONArray jsonArr = json.getJSONArray(k);
                    Iterator itArr = jsonArr.iterator();
                    while (itArr.hasNext()) {
                        JSONObject job = (JSONObject) itArr.next();
                        goalValue = getGoalValue(job, goalKey);
                        //①考虑目标结果要取一个json数组下不同对象的值
                        if (null != goalValue) {
                            list.add(goalValue.toString());
                        }
                        //②不考虑目标结果要多个值的情况（用①代码则注释②代码，反之）
						/*if (null != goalValue) {
							break;
						}*/
                    }
                    //①考虑目标结果要取一个json数组下不同对象的值
                    goalValue = list.toString().substring(1, list.toString().length() - 1);
                }
            }
        }
        return goalValue;
    }

    /**
     * @param str xml2Json转换方法
     * @return jsonObject对象
     */
    public static JSONObject xml2Json(String str) {
        if (StringUtils.isBlank(str)) {
            JSONObject json = new JSONObject();
            json.put("errorCode", 5555);
            json.put("errorMsg", "请求第三方错误或超时！");
            json.put("data", "");
            return json;
        }
        if (str.trim().startsWith("<") && str.contains(">") && (str.contains("</") || str.contains("/>"))) {
            return JSONObject.parseObject(XML.toJSONObject(str).toString());
        }
        return JSONObject.parseObject(str);
    }

    /**
     * 不对html解析成json ----目前针对BBD企业关联规则中企业司法拍卖信息API接口返回的json中含有html
     *
     * @param str noHtml2Json转换方法
     * @return jsonObject对象
     */
    public static JSONObject noHtml2Json(String str) {
        if (StringUtils.isBlank(str)) {
            JSONObject json = new JSONObject();
            json.put("errorCode", 5555);
            json.put("errorMsg", "请求第三方错误或超时！");
            json.put("data", "");
            return json;
        }
        return JSONObject.parseObject(str);
    }

    /**
     * 从json或xml获取指定值（单层值）
     *
     * @param string  json/xml
     * @param goalKey 目标值
     * @return
     */
    public static String getValue(String string, String goalKey) {
        JSONObject jsonString = xml2Json(string);
        String value = getGoalValue(jsonString, goalKey);
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return StringUtils.join(goalKey, ":", value);
    }

    /**
     * 从json或xml获取指定值（多层值）
     *
     * @param string json/xml
     * @param
     * @return
     */
    public static String getInValue(String string, String firstKey, String secondKey) {
        JSONObject jsonString = xml2Json(string);
        String value = getGoalValue(jsonString, firstKey);
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return getValue(value, secondKey);
    }
}
