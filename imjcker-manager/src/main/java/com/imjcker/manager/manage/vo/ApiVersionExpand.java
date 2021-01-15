package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.ApiInfoVersionsWithBLOBs;

/**

 * @Title ApiVersionExpand
 * @Description apiInfo版本表的包装类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月26日 下午2:36:20
 */
public class ApiVersionExpand extends ApiInfoVersionsWithBLOBs{
	/** 分组名称 */
	private String groupName;
	private String url;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
