package com.imjcker.manager.manage.vo;

import com.lemon.common.vo.BaseQuery;

/**

 * @Title ApiInfoQuery
 * @Description ApiInfo的查询类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月26日 下午2:36:49
 */
public class ApiInfoQuery extends BaseQuery{
	private String apiName;
	private Integer apiGroupId;
	private Integer env;
	private Integer apiId;
	//无分组查询的标注
	private Integer flagNoGroup;

	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public Integer getApiGroupId() {
		return apiGroupId;
	}
	public void setApiGroupId(Integer apiGroupId) {
		this.apiGroupId = apiGroupId;
	}
	public Integer getEnv() {
		return env;
	}
	public void setEnv(Integer env) {
		this.env = env;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	public Integer getFlagNoGroup() {
		return flagNoGroup;
	}
	public void setFlagNoGroup(Integer flagNoGroup) {
		this.flagNoGroup = flagNoGroup;
	}


}
