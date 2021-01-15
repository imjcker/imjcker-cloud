package com.imjcker.api.common.util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * <p>Title: BeanUtils
 * <p>Description: Bean转换工具类
 * <p>Copyright: Copyright (c) 2016
 * @author 周露
 * @date 2016-3-30 下午11:14:41
 * @version V1.0
 */
public class BeanCustomUtils {
	/**
	 *
	 * <p>Title: getNullPropertyNames
	 * <p>Description: 获取对象的空值属性数组
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

	 public static void copyPropertiesIgnoreNull(Object src, Object target){
	        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	 }

}
