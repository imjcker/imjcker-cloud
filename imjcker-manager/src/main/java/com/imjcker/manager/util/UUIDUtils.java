package com.imjcker.manager.util;
import java.util.UUID;

public class UUIDUtils {

    public static String getNormalUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");// 把-替换为空
    }
}
