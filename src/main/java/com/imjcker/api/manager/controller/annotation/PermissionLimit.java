package com.imjcker.api.manager.controller.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限限制
 * @author imjcker 2015-12-12 18:29:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {
	
	/**
	 * 要求用户登录
	 */
	boolean limit() default true;

	/**
	 * 要求管理员权限
	 */
	boolean superUser() default false;

}