package com.imjcker.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Title: GlobalVariable.java
 * <p>Description: 全局变量加载类
 * <p>Copyright: Copyright © 2017, CQzlll, All Rights Reserved.
 *
 * @author CQzlll.zl
 * @version 1.0
 */
public class GlobalVariable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariable.class);

    /** 全局配置环境 */
    public static String environment;

    static {
        try {
            InputStream globalIS = GlobalVariable.class.getClassLoader().getResourceAsStream("global.properties");
            Properties globalProperties = new Properties();
            globalProperties.load(globalIS);
            environment = globalProperties.getProperty("environment");
            LOGGER.debug("初始化全局配置参数成功.");
        } catch (IOException ex) {
            LOGGER.error("初始化全局配置参数失败.", ex);
        }
    }

    private GlobalVariable() {
        throw new RuntimeException("禁止实例化全局配置参数公共类.");
    }
}
