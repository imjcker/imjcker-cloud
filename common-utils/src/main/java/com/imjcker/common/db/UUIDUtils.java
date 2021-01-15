package com.imjcker.common.db;

import java.util.UUID;

public class UUIDUtils {

    /** UUID随机生成方法 */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");// 把-替换为空
    }
}
