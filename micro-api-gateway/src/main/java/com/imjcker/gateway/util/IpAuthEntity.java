package com.imjcker.gateway.util;

import java.io.Serializable;

/**
 * @author yezhiyuan lihongjie
 * @version V2.0
 * @Title: IP白名单校验数据实体类
 * @Package com.lemon.common.vo
 * @date 2018年1月22日 下午5:08:59
 */
public class IpAuthEntity implements Serializable {

    private static final long serialVersionUID = -4484360772079548499L;

    private String uid;

    private String ipAddress;

    private String appKey;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
