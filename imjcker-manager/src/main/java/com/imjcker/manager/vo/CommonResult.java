package com.imjcker.manager.vo;

/**
 * <p>Title: CommonResult.java
 * <p>Description: 通用数据返回对象
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
public class CommonResult<T> {

    /** 业务状态码 */
    private int code;
    /** 业务信息 */
    private String message;
    /** 数据 */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 常用构造方法
     * @param resultStatus 结果枚举
     * @param data 数据
     */
    public CommonResult(ResultStatusEnum resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
    }

    public CommonResult() {}

    public static CommonResult ex(ResultStatusEnum status) {
        return new CommonResult(status, null);
    }
    public static CommonResult error() {
        return CommonResult.error();
    }

    public static CommonResult success(Object data) {
        return CommonResult.success( data);
    }
    public static CommonResult success() {
        return CommonResult.success();
    }

    public static CommonResult ex(ResultStatusEnum status, Object message) {
        return new CommonResult(status, message);
    }

}
