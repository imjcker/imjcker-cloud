package com.imjcker.manager.manage.vo;

import java.util.List;

import com.imjcker.manager.manage.po.ApiGroup;

public class MultiLevelGroup extends ApiGroup{
	private List<MultiLevelGroup> children;

	public List<MultiLevelGroup> getChildren() {
		return children;
	}

	public void setChildren(List<MultiLevelGroup> children) {
		this.children = children;
	}

}
