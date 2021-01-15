package com.imjcker.api.common.vo;

import io.swagger.annotations.ApiModel;

/**
 * <p>Title: CommonResult.java
 * <p>Description: 通用数据返回对象
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
@ApiModel(value = "通用接口返回数据模型")
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public CommonResult(){}
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

    public CommonResult(ResultStatusEnum resultStatus) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = null;
    }

    public CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(){
        return new CommonResult(ResultStatusEnum.SUCCESS);
    }

    public static <T> CommonResult<T> success(T data){
        return new CommonResult(ResultStatusEnum.SUCCESS, data);
    }

    public static <T> CommonResult<T> fail(ResultStatusEnum status){
        return new CommonResult(status);
    }

    public static <T> CommonResult<T> ex(int code, String message, String data){
        return new CommonResult(code, message, data);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommonResult{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
