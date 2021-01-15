package com.imjcker.api.handler.model;

/**
 * 返回给客户端的Json模型
 */
public class JsonResult {
    private String uid;
    private int errorCode;
    private String errorMsg;
    private Object data;

    public JsonResult() {
        this(null);
    }

    public JsonResult(String orderId) {
        this(orderId, StatusCode.success);
    }

    public JsonResult(String orderId, String exception) {
        this.uid = orderId;
        if(exception.indexOf(':') == -1){
            this.errorMsg = "Exception: "+exception;
        }else{
            this.errorMsg = "Type:" + exception.substring(0, exception.indexOf(':')) + " Desc:" + exception.substring(exception.indexOf(':') + 1);
        }
        this.errorCode = 500;
    }

    /**
     * 正常的返回结果
     *
     * @param data
     */
    public JsonResult(String uid, Object data) {
        this(uid, StatusCode.success);
        this.data = data;
    }

    public JsonResult(String uid, StatusCode statusCode) {
        this.uid = uid;
        this.errorCode = statusCode.getCode();
        this.errorMsg = statusCode.getDesc();
    }

    public int getErroCode() {
        return errorCode;
    }

    public void setStatusCode(String uid, StatusCode statusCode) {
        this.uid = uid;
        this.errorCode = statusCode.getCode();
        this.errorMsg = statusCode.getDesc();
    }

    public void setErroCode(Integer erroCode) {
        this.errorCode = erroCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static enum StatusCode {
        success(1000, "请求成功"),
        error(1002, "系统错误"),
        requestError(1001, "请求第三方出错");

        private int code;
        private String desc;

        private StatusCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
