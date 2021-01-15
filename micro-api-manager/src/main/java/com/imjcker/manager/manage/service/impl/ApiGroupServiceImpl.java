package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.mapper.ApiInfoMapper;
import com.imjcker.manager.manage.vo.ApiGroupQuery;
import com.imjcker.manager.manage.vo.CommonInfo;
import com.imjcker.manager.manage.vo.MultiLevelGroup;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.ApiGroupMapper;
import com.imjcker.manager.manage.mapper.ApiGroupRelationMapper;
import com.imjcker.manager.manage.po.ApiGroup;
import com.imjcker.manager.manage.po.ApiGroupExample;
import com.imjcker.manager.manage.po.ApiGroupRelation;
import com.imjcker.manager.manage.po.ApiGroupRelationExample;
import com.imjcker.manager.manage.po.ApiInfoExample;
import com.imjcker.manager.manage.po.ApiInfoWithBLOBs;
import com.imjcker.manager.manage.service.ApiGroupService;
import com.imjcker.manager.manage.service.ApiService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**

 * @Title ApiGroupServiceImpl
 * @Description API分组服务实现层
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月12日 下午4:14:20
 */
@Transactional
@Service
public class ApiGroupServiceImpl implements ApiGroupService{

	@Autowired
	ApiGroupMapper apiGroupMapper;

	@Autowired
    ApiInfoMapper apiInfoMapper;

	@Autowired
	ApiService apiService;

	@Autowired
	ApiGroupRelationMapper apiGroupRelationMapper;

	@Override
	public Integer save(ApiGroup apiGroup) {
		String groupName = StringUtils.trim(apiGroup.getGroupName());
		String groupDescription = StringUtils.trim(apiGroup.getGroupDescription());
		groupDescription = groupDescription.replaceAll("\\s{2,}", " ");
		//检查分组名称唯一性，不唯一则抛出异常，返回
		ApiGroupQuery apiGroupQuery = new ApiGroupQuery();
		apiGroupQuery.setGroupName(groupName);
		if (!this.checkUnique(apiGroupQuery)) {
			throw new BusinessException(ExceptionInfo.EXIST_APIGROUPNAME);
		}
		//装载数据
		//保存层级关系和父节点
		Integer parentId = apiGroup.getParentId();
		if (null != parentId) {
			int higherLevel = this.findById(parentId).getGroupLevel();
			if (higherLevel < 3) {
				//检查分组是否直接包含了API
				ApiInfoExample example = new ApiInfoExample();
				ApiInfoExample.Criteria criteria = example.createCriteria();
				criteria.andStatusEqualTo(CommonStatus.ENABLE);
				criteria.andApiGroupIdEqualTo(parentId);
				List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExample(example);
				if (null != apiList && apiList.size() > 0) {
					throw new BusinessException(ExceptionInfo.THIS_GROUP_ALREADY_HAVE_API);
				}
				apiGroup.setGroupLevel(higherLevel + 1);
			} else {
				throw new BusinessException(ExceptionInfo.THREE_LEVEL_CANT_BUILD_GROUP);
			}
		} else {
			apiGroup.setGroupLevel(1);
		}
		apiGroup.setGroupName(groupName);
		apiGroup.setGroupDescription(groupDescription);
		apiGroup.setStatus(CommonStatus.ENABLE);
		apiGroup.setGroupDomainName(CommonInfo.DEFAULT_DOMAINNAME);
		apiGroup.setGroupUUID(UUID.randomUUID().toString().replaceAll("-", ""));
		//id自增，不管接收到任何id，均不保存
		apiGroup.setId(null);
		//设置创建和更新时间
		Long createTime = System.currentTimeMillis();
		apiGroup.setCreateTime(createTime);
		apiGroup.setUpdateTime(createTime);

		apiGroupMapper.insert(apiGroup);
		return apiGroup.getId();
	}

	@Override
	public void update(ApiGroup apiGroup) {
		String groupName = StringUtils.trim(apiGroup.getGroupName());
		String groupDescription = StringUtils.trim(apiGroup.getGroupDescription());
		groupDescription = groupDescription.replaceAll("\\s{2,}", " ");

		//检查分组名称唯一性，不唯一则抛出异常，返回
		ApiGroupQuery apiGroupQuery = new ApiGroupQuery();
		apiGroupQuery.setGroupName(groupName);
		apiGroupQuery.setGroupUUID(apiGroup.getGroupUUID());
		if (!this.checkUnique(apiGroupQuery)) {
			throw new BusinessException(ExceptionInfo.EXIST_APIGROUPNAME);
		}
		//通过ID从数据库查找分组信息
		ApiGroup oldApiGroup = this.findByUUID(apiGroup.getGroupUUID());
		//如果分组为空则抛出异常，不存在这样的分组
		if (null == oldApiGroup) {
			throw new BusinessException(ExceptionInfo.NOT_EXIST_GROUP);
		}
		//修改了分组名，检查是否与api有绑定关联， 并更新关系表api_group_relation的fullPathName字段
		String oldGroupName  = oldApiGroup.getGroupName();
		if (!groupName.equals(oldGroupName)) {
			ApiGroupRelationExample relationExample = new ApiGroupRelationExample();
			ApiGroupRelationExample.Criteria relationCriteria = relationExample.createCriteria();
			String path = StringUtils.wrap(StringUtils.join(oldApiGroup.getId().toString(),"."), "%");
			relationCriteria.andPathLike(path);
			//获取所有与该分组有关的关系数据
			List<ApiGroupRelation> relationList = apiGroupRelationMapper.selectByExample(relationExample);
			if (null != relationList && relationList.size() > 0) {
				//置换每条数据中修改的分组名
				for (ApiGroupRelation re : relationList) {
					String fullPathName = re.getFullPathName();
					String[] pathNames = fullPathName.split("\\.");
					for (int i = 0; i < pathNames.length; i++) {
						String pathName = pathNames[i];
						if (oldGroupName.equals(pathName)) {
							pathNames[i] = groupName;
							break;
						}
					}
					StringBuffer sb = new StringBuffer();
					int len = pathNames.length - 1;
					for (int k = 0; k < len; k++) {
						sb.append(pathNames[k]).append(".");
					}
					sb.append(pathNames[len]);
					re.setFullPathName(sb.toString());
					apiGroupRelationMapper.updateByPrimaryKey(re);
				}
			}
		}
		oldApiGroup.setGroupName(groupName);
		oldApiGroup.setGroupDescription(groupDescription);
		Long updateTime = System.currentTimeMillis();
		oldApiGroup.setUpdateTime(updateTime);
		apiGroupMapper.updateByPrimaryKey(oldApiGroup);
	}

	@Override
	public ApiGroup findByUUID(String groupUUID) {
		//如果ID为空则抛出异常，否则返回查询的分组信息
		if (null == groupUUID) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPUUID);
		}
		ApiGroupExample example = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = example.createCriteria();
		criteria.andGroupUUIDEqualTo(groupUUID);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<ApiGroup> groupList = apiGroupMapper.selectByExample(example);
		if (null == groupList || groupList.size() == 0) {
			throw new BusinessException(ExceptionInfo.NOT_EXSIT_API_GROUP);
		}
		return groupList.get(0);
	}

	/**
	 * 通过ID查找分组
	 * @param id
	 * @return ApiGroup
	 */
	@Override
	public ApiGroup findById(Integer id) {
		//如果ID为空则抛出异常，否则返回查询的分组信息
		if (null == id) {
			throw new DataValidationException(ExceptionInfo.NOT_EXIST_ID);
		}
		return apiGroupMapper.selectByPrimaryKey(id);
	}

//	@Override
//	public String logicDelete(String groupUUID) {
//		//通过groupUUID从数据库查找分组信息
//		ApiGroup oldApiGroup = this.findByUUID(groupUUID);
//		//如果分组为空则抛出异常，不存在这样的分组
//		if (null == oldApiGroup || CommonStatus.DISENABLE == oldApiGroup.getStatus()) {
//			throw new BusinessException(ExceptionInfo.NOT_EXIST_GROUP);
//		}
//		//检查分组下是否有正在运行的api
//		List<ApiInfoWithBLOBs> apiInfoList = apiService.findByGroupIdAndStatus(oldApiGroup.getId());
//		if (null != apiInfoList && apiInfoList.size() != 0) {
//			//判断是否有正在运行的api
//			ApiInfoWithBLOBs apiInfo;
//			Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
//			while (iterator.hasNext()) {
//				apiInfo = iterator.next();
//				if (CommonStatus.ENABLE == apiInfo.getPublishProductEnvStatus() || CommonStatus.ENABLE == apiInfo.getPublishTestEnvStatus()) {
//					throw new BusinessException(ExceptionInfo.DELETE_API_GROUP_FAILED);
//				}
//			}
//		}
//		oldApiGroup.setStatus(CommonStatus.DISENABLE);
//		oldApiGroup.setUpdateTime(System.currentTimeMillis());
//		Integer isSuccess = apiGroupMapper.updateByPrimaryKey(oldApiGroup);
//		if (0 == isSuccess) {
//			throw new BusinessException(ExceptionInfo.SQL_DB_EXCEPTION);
//		}
//		//如果分组下有api(未运行)则删除api
//		if (null != apiInfoList && apiInfoList.size() != 0) {
//			//判断是否有正在运行的api
//			ApiInfoWithBLOBs apiInfo;
//			Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
//			while (iterator.hasNext()) {
//				apiInfo = iterator.next();
//				apiService.delete(apiInfo);
//			}
//		}
//		return "删除成功";
//	}


//	@Override
//	public String logicDelete(String groupUUID) {
//		//通过groupUUID从数据库查找分组信息
//		ApiGroup oldApiGroup = this.findByUUID(groupUUID);
//		//如果分组为空则抛出异常，不存在这样的分组
//		if (null == oldApiGroup || CommonStatus.DISENABLE == oldApiGroup.getStatus()) {
//			throw new BusinessException(ExceptionInfo.NOT_EXIST_GROUP);
//		}
//		//删除级别下分组
//		this.logicDeleteGroup(oldApiGroup);
//		//删除相关API
//		this.logicDeleteRelationOfApi(oldApiGroup);
//		//检查分组下的api，并置空apiGroupId的值
//		List<ApiInfoWithBLOBs> apiInfoList = apiService.findByGroupIdAndStatus(oldApiGroup.getId());
//		if (null != apiInfoList && apiInfoList.size() != 0) {
//			//判断是否有正在运行的api
//			ApiInfoWithBLOBs apiInfo;
//			Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
//			while (iterator.hasNext()) {
//				apiInfo = iterator.next();
//				if (CommonStatus.ENABLE == apiInfo.getPublishProductEnvStatus() || CommonStatus.ENABLE == apiInfo.getPublishTestEnvStatus()) {
//					throw new BusinessException(ExceptionInfo.DELETE_API_GROUP_FAILED);
//				}
//				//置空API表的apiGroupId值
//				apiService.setGroupIdToNull(apiInfo.getId());
//			}
//		}
//		//【物理删除】-删除api与group之间的绑定
//		apiService.physicallyDeleteRelation(oldApiGroup.getId(),null,null);
//
//		oldApiGroup.setStatus(CommonStatus.DISENABLE);
//		oldApiGroup.setUpdateTime(System.currentTimeMillis());
//		Integer isSuccess = apiGroupMapper.updateByPrimaryKey(oldApiGroup);
//		if (0 == isSuccess) {
//			throw new BusinessException(ExceptionInfo.SQL_DB_EXCEPTION);
//		}
//		//如果分组下有api(未运行)则删除api
////		if (null != apiInfoList && apiInfoList.size() != 0) {
////			//判断是否有正在运行的api
////			ApiInfoWithBLOBs apiInfo;
////			Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
////			while (iterator.hasNext()) {
////				apiInfo = iterator.next();
////				apiService.delete(apiInfo);
////			}
////		}
//		return "删除成功";
//	}

	@Override
	public String logicDelete(String groupUUID) {
		//通过groupUUID从数据库查找分组信息
		ApiGroup oldApiGroup = this.findByUUID(groupUUID);
		//如果分组为空则抛出异常，不存在这样的分组
		if (null == oldApiGroup || CommonStatus.DISENABLE == oldApiGroup.getStatus()) {
			throw new BusinessException(ExceptionInfo.NOT_EXIST_GROUP);
		}
		//删除级别下分组
		this.logicDeleteGroup(oldApiGroup);
		//删除相关API
		this.logicDeleteRelationOfApi(oldApiGroup);
		oldApiGroup.setStatus(CommonStatus.DISENABLE);
		oldApiGroup.setUpdateTime(System.currentTimeMillis());
		Integer isSuccess = apiGroupMapper.updateByPrimaryKey(oldApiGroup);
		if (0 == isSuccess) {
			throw new BusinessException(ExceptionInfo.SQL_DB_EXCEPTION);
		}
		return "删除成功";
	}

	/**
	 * 删除相关API
	 * @param oldApiGroup
	 * @Version 2.0
	 */
	private void logicDeleteRelationOfApi(ApiGroup apiGroup) {
		String groupId = apiGroup.getId().toString();
		ApiGroupRelationExample example = new ApiGroupRelationExample();
		ApiGroupRelationExample.Criteria criteria = example.createCriteria();
		criteria.andPathLike(StringUtils.wrap(StringUtils.wrap(groupId, "."), "%"));
		//获取该分组下的api关系
		List<ApiGroupRelation> relationList = apiGroupRelationMapper.selectByExample(example);
		if (null != relationList && relationList.size() > 0) {
			//置空api的分组字段
			ApiGroupRelation relation;
			Iterator<ApiGroupRelation> iterator = relationList.iterator();
			while (iterator.hasNext()) {
				relation = iterator.next();
				apiService.setGroupIdToNull(relation.getApiId());
			}
		}
		//物理删除该关系
		apiService.physicallyDeleteRelation(null,null,groupId);
	}

	/**
	 * 逐层删除分组
	 * @param groupId
	 * @param groupLevel
	 * @Version 2.0
	 */
	private void logicDeleteGroup(ApiGroup childGroup) {
		List<ApiGroup> childGroupList;
		Integer groupLevel = childGroup.getGroupLevel();
		Integer groupId = childGroup.getId();
		while (groupLevel < 3) {
			ApiGroupExample example = new ApiGroupExample();
			ApiGroupExample.Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(groupId);
			criteria.andStatusEqualTo(CommonStatus.ENABLE);
			//获取下级分组列表
			childGroupList = apiGroupMapper.selectByExample(example);
			//存在下级分组
			if (null != childGroupList && childGroupList.size() > 0) {
				Iterator<ApiGroup> iterator = childGroupList.iterator();
				ApiGroup temp;
				while (iterator.hasNext()) {
					temp = iterator.next();
					this.logicDeleteGroup(temp);
				}
				//删除该级节点分组
				ApiGroup tmpGroup = new ApiGroup();
				tmpGroup.setStatus(CommonStatus.DISENABLE);
				tmpGroup.setUpdateTime(System.currentTimeMillis());
				apiGroupMapper.updateByExampleSelective(tmpGroup, example);
			}
			break;
		}
	}

	@Override
	public boolean checkUnique(ApiGroupQuery apiGroupQuery) {
		//查询条件为空，则返回false
		if (null == apiGroupQuery) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_QUERY);
		}
		ApiGroupExample apiGroupExample = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = apiGroupExample.createCriteria();
		//取出分组名称进行条件包装
		String groupName = apiGroupQuery.getGroupName();
		String groupUUID = apiGroupQuery.getGroupUUID();
		if (StringUtils.isBlank(groupName)) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
		}
		if (StringUtils.isNotBlank(groupUUID)) {
			criteria.andGroupUUIDNotEqualTo(groupUUID);
		}
		criteria.andGroupNameEqualTo(groupName);
		//未被删除的
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<ApiGroup> apiGroupList = apiGroupMapper.selectByExample(apiGroupExample);
		return apiGroupList == null || apiGroupList.size() == 0;
	}

	@Override
	public PageInfo<ApiGroup> findPageInfo(ApiGroupQuery apiGroupQuery) {
		ApiGroupExample apiGroupExample = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = apiGroupExample.createCriteria();
		//取出id和分组名称
		String uuid = apiGroupQuery.getGroupUUID();
		String groupName = apiGroupQuery.getGroupName();
		//判断非空，设置查询条件
		if (StringUtils.isNotBlank(uuid)) {
			criteria.andGroupUUIDEqualTo(uuid);
		}
		if (StringUtils.isNotBlank(groupName)) {
			groupName = groupName.replace("_", "\\_");
			criteria.andGroupNameLike(StringUtils.wrap(groupName, "%"));
		}
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		//按创建时间倒序排序，最新在最上面
		apiGroupExample.setOrderByClause("createTime DESC");
		Page apiGroupPage = PageHelper.startPage(apiGroupQuery.getPageNum(), apiGroupQuery.getPageSize());
		apiGroupMapper.selectByExample(apiGroupExample);
		PageInfo pageInfo = apiGroupPage.toPageInfo();
		List<ApiGroup> apiGroupList = apiGroupPage.getResult();
		pageInfo.setList(apiGroupList);
		return pageInfo;
	}


	@Override
	public List<ApiGroup> findAll() {
		ApiGroupExample example = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		example.setOrderByClause("createTime DESC");
		List<ApiGroup> apiGroupList = apiGroupMapper.selectByExample(example);
		return apiGroupList;
	}

	@Override
	public boolean hasNoneOfLowerLevelGroup(Integer groupId) {
		ApiGroupExample example = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(groupId);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<ApiGroup> groupList = apiGroupMapper.selectByExample(example);
		return null == groupList || groupList.size() == 0;
	}

	@Override
	public List<ApiGroup> listOfNoLowerLevelGroup() {
		ApiGroupExample example = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<ApiGroup> groupList = apiGroupMapper.selectByExample(example);
		if (null != groupList && groupList.size() > 0) {
			ApiGroup apiGroup;
			for (int i = 0; i <groupList.size(); i++) {
				apiGroup = groupList.get(i);
				ApiGroupExample example2 = new ApiGroupExample();
				ApiGroupExample.Criteria criteria2 = example2.createCriteria();
				criteria2.andParentIdEqualTo(apiGroup.getId());
				criteria2.andStatusEqualTo(CommonStatus.ENABLE);
				List<ApiGroup> list = apiGroupMapper.selectByExample(example2);
				if (null != list && list.size() > 0) {
					groupList.remove(i);
					i--;
				}
			}
		}
		return groupList==null? null: groupList.stream().sorted(Comparator.comparing(ApiGroup::getGroupName)).collect(Collectors.toList());
	}

	@Override
	public List<MultiLevelGroup> listByLevel(ApiGroup apiGroup) {
		ApiGroupExample apiGroupExample = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = apiGroupExample.createCriteria();
		//取出id和分组名称
		String uuid = apiGroup.getGroupUUID();
		String groupName = apiGroup.getGroupName();
		//判断非空，设置查询条件
		if (StringUtils.isNotBlank(uuid)) {
			criteria.andGroupUUIDEqualTo(uuid);
		}
		if (StringUtils.isNotBlank(groupName)) {
			groupName = groupName.replace("_", "\\_");
			criteria.andGroupNameLike(StringUtils.wrap(groupName, "%"));
		}
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		//按创建时间倒序排序，最新在最上面
		apiGroupExample.setOrderByClause("createTime DESC");
		List<ApiGroup> list = apiGroupMapper.selectByExample(apiGroupExample);
		List<MultiLevelGroup> groupList = new ArrayList<MultiLevelGroup>();
		if (null != list && list.size() > 0) {
			ApiGroup tempGroup;
			Iterator<ApiGroup> iterator = list.iterator();
			while (iterator.hasNext()) {
				MultiLevelGroup tempLevelGroup = new MultiLevelGroup();
				tempGroup = iterator.next();
				BeanCustomUtils.copyPropertiesIgnoreNull(tempGroup, tempLevelGroup);
				tempLevelGroup.setChildren(this.getgroupListByLevel(tempGroup.getGroupLevel() + 1, tempGroup));
				groupList.add(tempLevelGroup);
			}
		}
		return groupList;
	}

	private List<MultiLevelGroup> getgroupListByLevel(int level, ApiGroup group) {
		//创建最终容器
		List<MultiLevelGroup> groupList = new ArrayList<MultiLevelGroup>();
		if (level <= 3) {
			ApiGroupExample example = new ApiGroupExample();
			ApiGroupExample.Criteria criteria = example.createCriteria();
			criteria.andGroupLevelEqualTo(level);
			criteria.andStatusEqualTo(CommonStatus.ENABLE);
			if (null != group) {
				criteria.andParentIdEqualTo(group.getId());
			}
			List<ApiGroup> list = apiGroupMapper.selectByExample(example);
			if (null != list && list.size() > 0) {
				ApiGroup tempGroup;
				Iterator<ApiGroup> iterator = list.iterator();
				level++;
				while (iterator.hasNext()) {
					MultiLevelGroup tempLevelGroup = new MultiLevelGroup();
					tempGroup = iterator.next();
					BeanCustomUtils.copyPropertiesIgnoreNull(tempGroup, tempLevelGroup);
					tempLevelGroup.setChildren(this.getgroupListByLevel(level, tempGroup));
					groupList.add(tempLevelGroup);
				}
			}
		}
		return groupList;
	}


	@Override
	public PageInfo<MultiLevelGroup> pageInfoListByLevel(ApiGroupQuery apiGroupQuery) {
		ApiGroupExample apiGroupExample = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = apiGroupExample.createCriteria();
		//取出id和分组名称
		String uuid = apiGroupQuery.getGroupUUID();
		String groupName = apiGroupQuery.getGroupName();
		//判断非空，设置查询条件
		if (StringUtils.isNotBlank(uuid)) {
			criteria.andGroupUUIDEqualTo(uuid);
		}
		if (StringUtils.isNotBlank(groupName)) {
			groupName = groupName.replace("_", "\\_");
			criteria.andGroupNameLike(StringUtils.wrap(groupName, "%"));
		}
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		//按创建时间倒序排序，最新在最上面
		apiGroupExample.setOrderByClause("createTime DESC");
		Page apiGroupPage = PageHelper.startPage(apiGroupQuery.getPageNum(), apiGroupQuery.getPageSize());
		apiGroupMapper.selectByExample(apiGroupExample);
		PageInfo pageInfo = apiGroupPage.toPageInfo();
		List<ApiGroup> list = apiGroupPage.getResult();
		List<MultiLevelGroup> groupList = new ArrayList<MultiLevelGroup>();
		if (null != list && list.size() > 0) {
			ApiGroup tempGroup;
			Iterator<ApiGroup> iterator = list.iterator();
			while (iterator.hasNext()) {
				MultiLevelGroup tempLevelGroup = new MultiLevelGroup();
				tempGroup = iterator.next();
				BeanCustomUtils.copyPropertiesIgnoreNull(tempGroup, tempLevelGroup);
				tempLevelGroup.setChildren(this.getgroupListByLevel(tempGroup.getGroupLevel() + 1, tempGroup));
				groupList.add(tempLevelGroup);
			}
		}
		pageInfo.setList(groupList);
		return pageInfo;
	}

	@Override
	public PageInfo<ApiGroup> listByParentIdPageInfo(ApiGroupQuery apiGroupQuery) {
		ApiGroupExample apiGroupExample = new ApiGroupExample();
		ApiGroupExample.Criteria criteria = apiGroupExample.createCriteria();
		//取出id和分组名称
		Integer id = apiGroupQuery.getId();
		if (null != id) {
			ApiGroup apiGroup = this.findById(id);
			if (null == apiGroup) {
				throw new BusinessException(ExceptionInfo.NOT_EXIST_GROUP);
			}
			criteria.andParentIdEqualTo(apiGroupQuery.getId());
		} else {
			criteria.andGroupLevelEqualTo(1);
		}
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		//按创建时间倒序排序，最新在最上面
		apiGroupExample.setOrderByClause("createTime DESC");
		Page apiGroupPage = PageHelper.startPage(apiGroupQuery.getPageNum(), apiGroupQuery.getPageSize());
		apiGroupMapper.selectByExample(apiGroupExample);
		PageInfo pageInfo = apiGroupPage.toPageInfo();
		List<ApiGroup> apiGroupList = apiGroupPage.getResult();
		pageInfo.setList(apiGroupList);
		return pageInfo;
	}




}
