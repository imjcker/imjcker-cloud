package com.imjcker.gateway.util;

import java.io.Serializable;

/**
 * @Title: appKey白名单校验数据实体类
 * @Package com.lemon.common.vo
 * @author yezhiyuan
 * @date 2018年1月22日 下午5:08:59
 * @version V2.0
 */
public class AppkeyAuthEntity implements Serializable{

	private static final long serialVersionUID = -4258700423813910831L;

	private String uid;

	private String appKey;

	private Integer apiId;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
