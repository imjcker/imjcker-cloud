package com.imjcker.manager.manage.model;

import java.security.SecureRandom;

public class HouseManagerRequest {

    private String authenticationKey;

    private HouseManagerData data;

    private String loginid;

//    private String nonce = Integer.toString( new Random().nextInt());
    String nonce = String.valueOf(new SecureRandom().nextInt());

    private String password;

    private String regioncode;

    private String signature;

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public HouseManagerData getData() {
        return data;
    }

    public void setData(HouseManagerData data) {
        this.data = data;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
