package com.imjcker.api.common.vo;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.AddressUtils;

/**
 * 记录系统日志的Pojo
 */
public class LogPojo {
	//请求流水号
	private String requestUuid;
	//server的唯一标识,用来找到出错的机器
	private String serverId;
	//server的名称
	private String serverName;
	//appkey
	private String appkey;
	//ipv4
	private String ipv4;
	//api 对应的版本号
	private String apiVersionId;
	//日志类型
	private String logType;
	//日志信息
	private String msg;
	//日志详情,比如说错误堆栈等
	private String msgDetail;
	//请求的参数,或者返回的结果
	private Object param;


    private static ThreadLocal<LogPojo> threadInstance = new ThreadLocal<LogPojo>();
    /**
     * 初始化线程中需要记录日志的公共信息
     * @param requestUuid 请求流水号
     * @param serverId server的唯一标识,用来找到出错的机器
     * @param serverName server的名称
     * @param appkey appkey
     * @param apiVersionId api 对应的版本号
     * @return
     */
    public static LogPojo init(String requestUuid,String serverId,String serverName,String appkey,String apiVersionId){
    	LogPojo pojo = threadInstance.get();
    	if(pojo == null){
    		pojo = new LogPojo();
    		threadInstance.set(pojo);
    	}
    	pojo.requestUuid = requestUuid;
    	pojo.serverId = serverId;
    	pojo.serverName = serverName;
    	pojo.appkey = appkey;
    	pojo.apiVersionId = apiVersionId;
    	return pojo;
    }

    public static LogPojo init(String requestUuid,String serverId,String serverName){
    	LogPojo pojo = threadInstance.get();
    	if(pojo == null){
    		pojo = new LogPojo();
    		threadInstance.set(pojo);
    	}
    	pojo.requestUuid = requestUuid;
    	pojo.serverId = serverId;
    	pojo.serverName = serverName;
    	return pojo;
    }

    public static LogPojo getLog() {
		return threadInstance.get();
	}

    private static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
	}

    /**
     * {@link #getLogMsg(LogType, String, String, Object)}
     * @see LogType#info
     */
    public static String getInfoLogMsg(String msg,Object param){
    	return getLogMsg(LogType.info, msg, param,"");

    }

    /**
     * {@link #getLogMsg(LogType, String, String, Object)}
     * @see LogType#success
     *
     */
    public static String getSuccessLogMsg(String msg,Object param){
    	return getLogMsg(LogType.success, msg, param, "");
    }

    /**
     * {@link #getLogMsg(LogType, String, String, Object)}
     * @see LogType#error
     */
    public static String getErrorLogMsg(String msg,Object param,String... msgDetails){
        StringBuilder builder = new StringBuilder("");
        if(msgDetails != null){
        	for (String s : msgDetails) {
				builder.append(s);
			}
        }
    	String msgDetail = builder.toString();
		return getLogMsg(LogType.error, msg,param,msgDetail);
    }

    /**
     * {@link #getLogMsg(LogType, String, String, Object)}
     */
    public static String getErrorLogMsg(String msg,Object param,Throwable e){
    	String  msgDetail = getExceptionMsg(e);
    	return getLogMsg(LogType.error, msg,param,msgDetail);
    }

	/**
     * 获取消息的JSon字符串
     * @param logType 日志类型{@link LogType}
     * @param msg 日志信息
     * @param msgDetail 日志详情,比如说错误堆栈等
     * @param param 请求的参数,或者返回的结果
     * @return
     */
    private static String getLogMsg(LogType logType,String msg,Object param,String msgDetail){
    	LogPojo pojo = threadInstance.get();
    	if(pojo == null){
    		pojo = new LogPojo();
    		threadInstance.set(pojo);
    	}

    	pojo.logType = logType.toString();
    	pojo.msg = msg;
    	pojo.msgDetail = msgDetail;
    	pojo.param = param;

    	return String.format("monitorMsg{%s}", pojo.toNonJsonString());
    }

    private String toNonJsonString(){
    	return  new StringBuilder()
				.append("apiVersionId:").append(this.apiVersionId).append(",")
				.append("appKey:").append(this.appkey).append(",")
				.append("logType:").append(this.logType).append(",")
				.append("msg:").append(this.msg).append(",\r\n")
				.append("param:").append(JSONObject.toJSONString(this.param)).append("\r\n")
				.append(this.msgDetail)
				.toString();
	}

	/**
	 * 获取服务的唯一标识符
	 * @param request
	 * @return IP:Port
	 */
	public static String getServerId(HttpServletRequest request) {
		String ip = AddressUtils.getLocalIP();
		int port = AddressUtils.getPort(request);
		return String.format("%s:%d", ip,port);
	}

	public String getRequestUuid() {
		return requestUuid;
	}

	public String getServerId() {
		return serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public String getAppkey() {
		return appkey;
	}

	public String getApiVersionId() {
		return apiVersionId;
	}

	public String getLogType() {
		return logType;
	}

	public String getMsg() {
		return msg;
	}

	public String getMsgDetail() {
		return msgDetail;
	}

	public Object getParam() {
		return param;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setApiVersionId(String apiVersionId) {
		this.apiVersionId = apiVersionId;
	}



	/**
	 * 日志类型
	 */
	private static enum LogType{
		/**
		 * 成功日志,在本系统最后一步来打印
		 */
		success,
		/**
		 * 错误日志
		 */
		error,
		/**
		 * 普通日志
		 */
		info;
	}
}
