package com.imjcker.api.handler.plugin.resourceCache;

/**
 * 资源
 */
public interface IResource {
	/**
	 * 释放资源
	 */
	public void relase();
	/**
	 * 是否可用
	 */
	public boolean isAvalable();

	/**
	 * push数据(可以转为json字符串)到资源中,
	 * 如果push失败,需要释放资源
	 */
	public boolean push(IJsonSerializer model);

}
