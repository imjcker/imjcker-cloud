package com.imjcker.api.handler.util;

import java.util.UUID;

/**
 * Created by lilinfeng on 2017/7/18.
 */
public class UUIDUtil {
    public static String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
