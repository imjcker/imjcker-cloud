package com.imjcker.manager.manage.validator;

import com.imjcker.manager.manage.po.ApiGroup;
import com.imjcker.manager.manage.vo.ApiGroupQuery;
import com.lemon.common.exception.ExceptionInfo;
import org.apache.commons.lang3.StringUtils;

import com.lemon.common.exception.vo.DataValidationException;

/**

 * @Title GroupValidate
 * @Description 对分组的校验
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月13日 上午9:30:38
 */
public class GroupValidate {
	/**
	 * 对分组的数据进行参数校验
	 * @param apiGroup
	 */
	public static void GroupCheck(ApiGroup apiGroup) {
		String groupName = StringUtils.trim(apiGroup.getGroupName());
		String groupDescription = StringUtils.trim(apiGroup.getGroupDescription());

		if (StringUtils.isBlank(groupName)) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPNAME);
		} else if (!(groupName.length() >= 2 && groupName.length() <= 15)) {
				throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_APIGROUPNAME);
			}


		if (StringUtils.isBlank(groupDescription)) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_GROUPDESCRIPTION);
		} else if (!(groupDescription.length() > 0 && groupDescription.length() <= 180)) {
				throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_GROUPDESCRIPTION);
			}

	}

	/**
	 * 对ID进行为空和类型检验
	 * @param jsonObject
	 */
	public static void idCheck(ApiGroup apiGroup) {
		if (null == apiGroup) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUP);
		}
		if (null == apiGroup.getId()) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
		}
	}

	/**
	 * 对ID进行为空和类型检验
	 * @param
	 */
	public static void idCheck(ApiGroupQuery apiGroupQuery) {
		if (null == apiGroupQuery) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUP);
		}
		if (null == apiGroupQuery.getId()) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
		}
	}

	/**
	 * 对ID进行为空和类型检验
	 * @param jsonObject
	 */
	public static void UUIDCheck(ApiGroup apiGroup) {
		if (null == apiGroup) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUP);
		}
		if (null == apiGroup.getGroupUUID()) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPUUID);
		}
	}


}
