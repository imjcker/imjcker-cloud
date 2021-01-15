package com.imjcker.spring.cloud.service.elastic.utils;

import java.util.UUID;

public class UuidUtils {
    public UuidUtils() {
    }
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
