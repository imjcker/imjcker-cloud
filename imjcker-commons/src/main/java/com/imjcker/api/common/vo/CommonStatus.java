package com.imjcker.api.common.vo;

/**
 * <p>Title: CommonStatus.java
 * <p>Description: 通用状态常量
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author zl
 * @version 1.0
 */
public interface CommonStatus {
    /** 状态：启用、是、正常 */
    Integer ENABLE = 1;
    /** 状态：停用、否、删除、未发布 */
    Integer DISENABLE = 2;

    /** 参数数据类型之封装类：String */
    Integer PARAMTYPE_STRING = 1;

    /** 运行环境：线上 */
    Integer ONLINE = 1;
    /** 运行环境：预发 */
//    Integer ADVANCE = 2;

    /** 参数类型：变量 */
    Integer VARIABLE = 1;
    /** 参数类型：常量 */
    Integer CONSTANT = 2;
    /** 参数类型：json */
    Integer JSON = 3;
    /** 参数类型：xml */
    Integer XML = 4;

    /** 请求方式：get */
    Integer GET = 1;
    /** 参数类型：post */
    Integer POST = 2;

    /** 参数位置：header */
    Integer HEADER = 1;
    /** 参数位置：query */
    Integer QUERY = 2;
    /** 参数位置：body */
    Integer BODY = 3;






}
