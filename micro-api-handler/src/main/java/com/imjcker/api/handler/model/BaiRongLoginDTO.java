package com.imjcker.api.handler.model;

import java.io.Serializable;

public class BaiRongLoginDTO implements Serializable {

    /*
        接入方编码
     */
    private String apiCode;
    /*
        登陆地址
     */
    private String loginUrl;
    /*
        用户名
     */
    private String userName;
    /*
        密码
     */
    private String password;
    /*
        接口分类名称
     */
    private String apiName;

    private String category;

    private String method;

    public BaiRongLoginDTO(String apiCode, String loginUrl, String userName, String password, String apiName, String category, String method) {
        this.apiCode = apiCode;
        this.loginUrl = loginUrl;
        this.userName = userName;
        this.password = password;
        this.apiName = apiName;
        this.category = category;
        this.method = method;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
