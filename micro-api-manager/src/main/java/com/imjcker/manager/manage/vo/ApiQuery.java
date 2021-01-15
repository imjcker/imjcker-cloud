package com.imjcker.manager.manage.vo;

import com.lemon.common.vo.BaseQuery;

/**

 * @Title ApiQuery
 * @Description api查询类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月17日 上午11:24:41
 */
public class ApiQuery extends BaseQuery{
	private String apiName;
	private Integer apiId;
	private Integer apiGroupId;
	//接口2.0------下面----------------
	private String interfaceName;
	private String backEndPath;
	/** 子上游API的ID */
	private Integer disId;
	//接口2.0------上面----------------

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

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	//接口2.0------下面----------------
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public Integer getDisId() {
		return disId;
	}

	public void setDisId(Integer disId) {
		this.disId = disId;
	}

	public String getBackEndPath() {
		return backEndPath;
	}

	public void setBackEndPath(String backEndPath) {
		this.backEndPath = backEndPath;
	}

	//接口2.0------上面----------------


}
