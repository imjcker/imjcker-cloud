package com.imjcker.api.handler.model;

/**
 * 房管局
 */
public class HouseManagerDTO {

    /**
     * 验证签名的key
     */
    private String authenticationKey;
    /**
     * 校验码
     */
    private String signature;
    /**
     * 账号
     */
    private String loginid;
    /**
     * 密码
     */
    private String password;
    /**
     * 区域代码
     */
    private String regioncode;
    /**
     * 随机码
     */
    private String nonce;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 数据（业务参数）
     */
    private HouseData data;

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
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

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public HouseData getData() {
        return data;
    }

    public void setData(HouseData data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
