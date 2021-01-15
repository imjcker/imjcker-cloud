package com.imjcker.api.handler.strategy;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.ExitParamModel;
import com.imjcker.api.handler.util.ExtractUtil;
import com.imjcker.api.handler.model.ExitParamModel;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.util.ExtractUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

/**
 * @author thh  2019/7/19
 * @version 1.0.0
 **/
public interface ApiStrategy {

    String OKHTTP = "okHttp";
    String HTTPCLIENT = "httpClient";

    /**
     * 处理请求参数,处理传入到具体业务Service中的需要发送的参数
     *
     * @return 返回请求参数字符串
     * @throws IOException              IO
     * @throws NoSuchAlgorithmException al
     */
    String handleParams(MainEntity.Params params, MainEntity mainEntity, Map<String, Object> jsonConfig, String url) throws IOException, NoSuchAlgorithmException;

    /**
     * 处理第三方原始返回信息,将第三方返回的数据处理成JSONObject 对象
     *
     * @param result 第三方返回结果
     * @return result的JSONObject
     * @throws IOException IO
     */
    default JSONObject handleResult(String result) throws IOException, JSONException {
        if (StringUtils.isBlank(result)) {
            return getNullResultJsonObject();
        }
        org.json.JSONObject object = new org.json.JSONObject(result);
        Integer code = object.getInt("code");
        if (Objects.equals(2000, code)) {
            String data = object.getString("data");
            return ExtractUtil.xml2Json(data);
        }
        return JSONObject.parseObject(result);
    }

    default JSONObject getNullResultJsonObject() {
        JSONObject object = new JSONObject();
        object.put("errorCode", 500);
        object.put("errorMsg", "第三方数据返回为空,请检查参数");
        return object;
    }

    /**
     * 获取ApiStrategy的具体实现类在spring容器中的实例.
     *
     * @return ApiStrategyMapping
     */
    ApiStrategyMapping getStrategy();

    /**
     * 第三方数据源原始调用,组合接口模块中调用
     *
     * @param api
     * @param params
     * @param retryFlag
     * @param retryCount
     * @param map
     * @return
     */
    default String doRequest(ApiInfoVersions api, MainEntity.Params params, String retryFlag,
                             int retryCount, Map<String, Object> map, String environment) throws Exception {
        return null;
    }

    default JSONObject buildJson(MainEntity.Params params, String url, String method, String uid, Long timeout,
                                 String protocol, String retryFlag, Integer retryCount, String type,
                                 boolean isIgnoreVerify, Integer apiId, String apiName) {
//        JSONObject json = new JSONObject();
//        JSONObject data = new JSONObject();
//        data.put("headerList", params.getHeaderVariables());
//        data.put("queryList", params.getQueryVariables());
//        data.put("bodyList", params.getBodyVariables());
//        data.put("json", params.getJson());
//        data.put("xml", params.getXml());
//        json.put("url", url);
//        json.put("data", data);
//        json.put("method", method);
//        json.put("uid", uid);
//        json.put("timeout", timeout);
//        json.put("protocol", protocol);
//        json.put("retryFlag", retryFlag);
//        json.put("retryCount", retryCount);
//        json.put("type", type);
//        json.put("isIgnoreVerify", isIgnoreVerify);
        ExitParamModel.Data data = new ExitParamModel.Data();
        data.setHeaderList(params.getHeaderVariables());
        data.setQueryList(params.getQueryVariables());
        data.setJson(params.getJson());
        data.setXml(params.getXml());
        data.setBodyList(params.getBodyVariables());
        ExitParamModel model = new ExitParamModel();
        model.setData(data);
        model.setUrl(url);
        model.setMethod(method);
        model.setUid(uid);
        model.setTimeout(timeout);
        model.setProtocol(protocol);
        model.setRetryFlag(retryFlag);
        model.setRetryCount(retryCount);
        model.setType(type);
        model.setIgnoreVerify(isIgnoreVerify);
        model.setApiId(apiId);
        model.setApiName(apiName);
        return JSONObject.parseObject(JSONObject.toJSONString(model));
    }

}
