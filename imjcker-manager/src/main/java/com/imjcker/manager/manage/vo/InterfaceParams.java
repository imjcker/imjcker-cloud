package com.imjcker.manager.manage.vo;

import java.util.List;

/**

 * @Title 一个上游接口名称对应的多个参数list
 * @Description
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 2.0
 * 2018年3月6日 下午5:13:05
 */
public class InterfaceParams {
	public String interfaceName;
	public List<BackendParamsExpand> paramList;
	public Integer disId;

	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public List<BackendParamsExpand> getParamList() {
		return paramList;
	}
	public void setParamList(List<BackendParamsExpand> paramList) {
		this.paramList = paramList;
	}
	public Integer getDisId() {
		return disId;
	}
	public void setDisId(Integer disId) {
		this.disId = disId;
	}


}
