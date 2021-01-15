package com.imjcker.api.handler.plugin.resourceCache;

/**
 * 资源的操作工具
 */
public interface IResourceHandler {
	/**
	 * 创建资源
	 */
	public IResource creat();
	/**
	 * 获取resource的唯一标识,相同值的认为是同一资源
	 */
	public String getResourceId();
}
