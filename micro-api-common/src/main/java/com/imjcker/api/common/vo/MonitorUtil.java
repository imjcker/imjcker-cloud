package com.imjcker.api.common.vo;

/**
 * @Title: 日志信息暂存类
 * @Package com.lemon.common.vo
 * @author yezhiyuan
 * @date 2017年7月20日 下午2:29:49
 * @version V2.0
 */
public class MonitorUtil {

    private static ThreadLocal<MonitorMsgPoJo> logJson = new ThreadLocal<MonitorMsgPoJo>();

    private static ThreadLocal<StringBuilder> logMsg = new ThreadLocal<StringBuilder>();

    public static void removeLogJson() {
    	logJson.remove();
	}

    public static void removeLogMsg() {
    	logMsg.remove();
	}

    public static void putLog(MonitorMsgPoJo poJo) {
        logJson.set(poJo);
    }

    public static void putIpv4(String ipv4) {
    	MonitorMsgPoJo pojo = getLog();
    	pojo.setIpv4(ipv4);
        logJson.set(pojo);
    }

    public static void putLogParams(Object requestParams) {
        MonitorMsgPoJo poJo = getLog();
    	poJo.setRequestParams(requestParams);
        logJson.set(poJo);
    }

    public static void putLogInit(String uid, String url,String appKey){
        MonitorMsgPoJo pojo = new MonitorMsgPoJo();
        pojo.setRequestUuid(uid);
        pojo.setRequestTime(System.currentTimeMillis());
        pojo.setUrl(url);
        pojo.setAppkey(appKey);
        logJson.set(pojo);
    }

    /**
     * 设置规则uuid和单价
     * @param billingRulesUuid
     * @param price
     */
    public static void putLogBillingRulesUuidAndPrice(String billingRulesUuid, double price) {
    	MonitorMsgPoJo poJo = getLog();
    	poJo.setBillingRulesUuid(billingRulesUuid);
    	poJo.setPrice(price);
        logJson.set(poJo);
    }

    public static MonitorMsgPoJo getLog() {
        return logJson.get();
    }

    public static void putMsg(StringBuilder msg) {
    	logMsg.set(msg);
    }

    public static void putLogApiIdAndVersionId(Integer apiId, String versionId,String apiName) {
        MonitorMsgPoJo poJo = getLog();
        poJo.setApiId(apiId);
        poJo.setVersionId(versionId);
        poJo.setApiName(apiName);
        logJson.set(poJo);
    }

    public static StringBuilder putMsg() {
        return logMsg.get();
    }
}
