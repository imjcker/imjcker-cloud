package com.imjcker.manager.manage.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiGroup;
import com.imjcker.manager.manage.vo.ApiGroupQuery;
import com.imjcker.manager.manage.vo.MultiLevelGroup;

/**

 * @Title ApiGroupService
 * @Description API分组服务接口
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月12日 下午4:13:36
 */
public interface ApiGroupService {

	/**
	 * 保存新建的分组，并返回该分组ID
	 * @param apiGroup
	 * @return Integer
	 */
	Integer save(ApiGroup apiGroup);

	/**
	 * 更新分组
	 * @param apiGroup
	 */
	void update(ApiGroup apiGroup);

	/**
	 * 根据ID查找对应的分组信息
	 * @param id
	 * @return ApiGroup
	 */
	ApiGroup findById(Integer id);

	/**
	 * 逻辑删除分组
	 * @param groupUUID
	 */
	String logicDelete(String groupUUID);

	/**
	 * 检查分组名称唯一性
	 * @param apiGroupQuery
	 * @return boolean
	 */
	boolean checkUnique(ApiGroupQuery apiGroupQuery);

	/**
	 * 分页查询分组列表，或条件查询
	 * @param apiGroupQuery
	 * @return PageInfo<ApiGroup>
	 */
	PageInfo<ApiGroup> findPageInfo(ApiGroupQuery apiGroupQuery);

	/**
	 * 通过UUID查找apiGroup
	 * @param groupUUID
	 * @return
	 */
	ApiGroup findByUUID(String groupUUID);

	/**
	 * 返回所有的分组
	 * @return
	 */
	List<ApiGroup> findAll();

	/**
	 * 检查分组是否存在下级分组
	 * @param groupId
	 * @return
	 * @Version 2.0
	 */
	boolean hasNoneOfLowerLevelGroup(Integer groupId);

	/**
	 * 按照层级结构返回分组
	 * @param apiGroup
	 * @return
	 * @Version 2.0
	 */
	List<MultiLevelGroup> listByLevel(ApiGroup apiGroup);

	/**
	 * 分页按照层级结构返回分组
	 * @param apiGroupQuery
	 * @return
	 * @Version 2.0
	 */
	PageInfo<MultiLevelGroup> pageInfoListByLevel(ApiGroupQuery apiGroupQuery);

	/**
	 * 返回可以绑定API的分组
	 * @return
	 * @Version 2.0
	 */
	List<ApiGroup> listOfNoLowerLevelGroup();

	/**
	 * 返回某一分组下的内容
	 * @param apiGroupQuery
	 * @return
	 * @Version 2.0
	 */
	PageInfo<ApiGroup> listByParentIdPageInfo(ApiGroupQuery apiGroupQuery);

}
