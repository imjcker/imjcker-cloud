package com.imjcker.api.handler.po;

/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年8月4日 下午3:40:26
 * @Title RequestParamAndValue
 * @Description 用于存储【调试API】的参数值
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
public class RequestParamAndValue {
    private String paramName;
    private String paramValue;

    public RequestParamAndValue() {

    }

    public RequestParamAndValue(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
