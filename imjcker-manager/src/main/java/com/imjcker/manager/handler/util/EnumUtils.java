package com.imjcker.api.handler.util;

/**
 * 枚举操作的工具类
 */
public class EnumUtils {


    public static interface IValueProperty {
        int getValue();
    }

    public static <T extends IValueProperty> T valueOf(int value, T[] values) {
        for (T t : values) {
            if (t.getValue() == value) {
                return t;
            }
        }

        throw new RuntimeException("枚举值不存在");
    }
}
