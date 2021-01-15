package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.BackendDistributeParams;

public class BackendParamsExpand extends BackendDistributeParams{
	/*上游接口参数名称*/
	private String interfaceName;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

}
