package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.ApiInfoWithBLOBs;

/**

 * @Title ApiExpand
 * @Description api发布时的拓展类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月19日 下午2:43:38
 */
public class ApiExpand extends ApiInfoWithBLOBs{
	/** 发布环境：线上；测试 */
	private Integer env;
	/** 发布备注 */
	private String pubDescription;
	/** 对应分组域名 */
	private String groupDomainName;
	/** 分组域名+httpPath */
	private String url;
	/** 分组域名称 */
	private String groupName;
	/** 策略名称 */
	private String StrategyName;

	/** 下线备注 */
	private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getEnv() {
		return env;
	}
	public void setEnv(Integer env) {
		this.env = env;
	}
	public String getPubDescription() {
		return pubDescription;
	}
	public void setPubDescription(String pubDescription) {
		this.pubDescription = pubDescription;
	}
	public String getGroupDomainName() {
		return groupDomainName;
	}
	public void setGroupDomainName(String groupDomainName) {
		this.groupDomainName = groupDomainName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getStrategyName() {
		return StrategyName;
	}
	public void setStrategyName(String strategyName) {
		StrategyName = strategyName;
	}


}
