package com.imjcker.manager.manage.vo;

/**

 * @Title APIVersionStrategy
 * @Description 用于接受已发布api绑定限流策略
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 2.0
 * 2017年9月19日 上午11:12:26
 */
public class ApiVersionStrategy {

	private String versionId;
	private String limitStrategyuuid;

	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getLimitStrategyuuid() {
		return limitStrategyuuid;
	}
	public void setLimitStrategyuuid(String limitStrategyuuid) {
		this.limitStrategyuuid = limitStrategyuuid;
	}

}
