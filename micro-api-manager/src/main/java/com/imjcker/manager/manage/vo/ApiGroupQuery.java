package com.imjcker.manager.manage.vo;

import com.lemon.common.vo.BaseQuery;

/**

 * @Title ApiGroupQuery
 * @Description API分组查询类
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月14日 上午9:33:35
 */
public class ApiGroupQuery extends BaseQuery{

	private Integer id;

	private String groupName;

	private String groupUUID;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupUUID() {
		return groupUUID;
	}

	public void setGroupUUID(String groupUUID) {
		this.groupUUID = groupUUID;
	}

}
