package com.imjcker.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: WebLogUtils.java
 * <p>Description: Web类日志工具类
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author zl
 * @version 1.0
 */
public class WebLogUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebLogUtils.class);

    /** 类型：开始 */
    private static final int TYPE_BEGIN = 0;
    /** 类型：一般 */
    private static final int TYPE_NORMAL = 1;
    /** 类型：警告 */
    private static final int TYPE_WARN = 2;
    /** 类型：错误 */
    private static final int TYPE_ERROR = 3;
    /** 类型：结束 */
    private static final int TYPE_END = 4;

    /** 分类：权限 */
    public static final int CATEGORY_PRIVILEGE = 1;

    /** 分类：计费 */
    public static final int CATEGORY_CHARGE = 2;

    /**
     * 记录日志开始的第一条
     * @param uri 请求URI
     * @param method 请求方式
     * @param targetMethod 目标方法
     * @param params 携带的参数
     */
    public static void begin(String uri,String method, String targetMethod, String params) {
        logger.info("请求信息:\r\nuri: {}\r\nmethod: {}\r\ntargetMethod: {}\r\nparams: {}",
                    uri,
                    method,
                    targetMethod,
                    params);
    }

    /**
     * 记录日志：普通类型
     * @param busKey 唯一标识码
     * @param msg 日志信息
     * @param params 携带的参数
     * @param category 目录所属类别
     */
    public static void normal(String busKey, String msg, String params, int category) {
        common(busKey, msg, params, category, TYPE_NORMAL);
    }

    /**
     * 记录日志：警告类型
     * @param busKey 唯一标识码
     * @param msg 日志信息
     * @param params 携带的参数
     * @param category 目录所属类别
     */
    public static void warn(String busKey, String msg, String params, int category) {
        common(busKey, msg, params, category, TYPE_WARN);
    }

    /**
     * 记录日志：错误类型
     * @param msg 日志信息
     */
    public static void error(String msg) {
//        common(busKey, msg, params, category, TYPE_ERROR);
        logger.error("异常信息: \r\n{}",msg);
    }

    /**
     * 记录日志：结束类型
     */
    public static void end() {
//        common(busKey, msg, params, category, TYPE_END);
//        logger.info("==========调用结束==========");
    }
    /**
     * 记录日志：结束类型
     */
    public static void end(String result) {
//        common(busKey, msg, params, category, TYPE_END);
//        logger.info("==========调用结束==========");
        logger.info("");
    }

    /**
     * 通用日志记录
     * @param busKey 唯一标识码
     * @param msg 日志信息
     * @param params 携带的参数
     * @param category 目录所属类别
     * @param type 日志类型
     */
    private static void common(String busKey, String msg, String params, int category, int type) {
        logger.info("WebLog{\"busKey\": {}, \"msg\": {}, \"params\": {}, \"category\": {}, \"type\": {}}",
                busKey, msg, params, category, type);
    }
}
