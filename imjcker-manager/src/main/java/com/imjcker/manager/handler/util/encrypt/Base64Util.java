package com.imjcker.api.handler.util.encrypt;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author WT
 * @Date 9:04 2020/4/9
 * @Version Base64Util v1.0
 * @Desicrption Base64 编解码
 */
public class Base64Util {

    public static String encode(String source) {
        return encode(source.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] source) {
        return new Base64Encoder().encode(source);
    }

    public static byte[] decodeByte(String source) {
        return new Base64Encoder().decode(source);
    }

    public static String decode(String source) {
        return Arrays.toString(decodeByte(source));
    }

}
