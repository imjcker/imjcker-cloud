package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.InterfaceCountModel;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.model.InterfaceCountModel;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 收集需要异步发送的信息
 * @Package com.lemon.client.util
 * @date 2017年11月7日 下午3:46:56
 */
public class AsyncCollectionUtil {

    private static ThreadLocal<AsyncModel> async = new ThreadLocal<>();

    private static ThreadLocal<Integer> charge = new ThreadLocal<>();

    private static ThreadLocal<InterfaceCountModel> countModel = new ThreadLocal<>();

    public static void removeAsync() {
        async.remove();
    }

    public static void removeCharge() {
        charge.remove();
    }

    public static void putAsyncModel(AsyncModel am) {
        async.set(am);
    }

    public static AsyncModel getAsyncModel() {
        return async.get();
    }

    public static void putCountModel(InterfaceCountModel am) {
        countModel.set(am);
    }

    public static InterfaceCountModel getCountModel() {
        return countModel.get();
    }

    public static void putCharge(Integer cha) {
        charge.set(cha);
    }

    public static Integer getCharge() {
        return charge.get();
    }

    public static void putAsyncUrl(String url) {
        AsyncModel asyncModel = getAsyncModel();
        asyncModel.setUrl(url);
        async.set(asyncModel);
    }

    public static void putAsyncParams(Object params) {
        AsyncModel asyncModel = getAsyncModel();
        asyncModel.setParams(params);
        async.set(asyncModel);
    }

    public static void putAsyncInitResult(String initResult) {
        AsyncModel asyncModel = getAsyncModel();
        asyncModel.setInitResult(initResult);
        async.set(asyncModel);
    }

    public static void putAsyncTrueResult(JSONObject trueResult) {
        AsyncModel asyncModel = getAsyncModel();
        asyncModel.setTrueResult(trueResult);
        async.set(asyncModel);
    }

    public static String getUid() {
        return getAsyncModel().getUid();
    }
}
