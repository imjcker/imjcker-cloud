package com.imjcker.manager.manage.vo;

import com.lemon.common.vo.BaseQuery;

/**

 * @Title ApiInfoVersionQuery
 * @Description apiInfo版本表查询类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月26日 下午2:37:19
 */
public class ApiInfoVersionQuery extends BaseQuery{
	/** api名称 */
	private String apiName;
	/** apiID */
	private Integer apiId;
	/** apiGroupID */
	private Integer apiGroupId;

	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	public Integer getApiGroupId() {
		return apiGroupId;
	}
	public void setApiGroupId(Integer apiGroupId) {
		this.apiGroupId = apiGroupId;
	}


}
