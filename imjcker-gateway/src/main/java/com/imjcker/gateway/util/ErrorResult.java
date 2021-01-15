package com.imjcker.gateway.util;

import com.imjcker.gateway.po.CompanyAppsAuth;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 第一层网关返回对象
 * @Package com.lemon.zuul.util
 * @date 2017年7月12日 下午2:24:44
 */
public class ErrorResult {

    private int errorCode;

    private String errorMsg;

    private String uid;

    private CompanyAppsAuth companyAppsAuth;

    public CompanyAppsAuth getCompanyAppsAuth() {
        return companyAppsAuth;
    }

    public void setCompanyAppsAuth(CompanyAppsAuth companyAppsAuth) {
        this.companyAppsAuth = companyAppsAuth;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int erroCode) {
        this.errorCode = erroCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
