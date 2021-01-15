package com.imjcker.api.handler.util.encrypt;

import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author WT
 * @Date 14:17 2020/4/9
 * @Version HmacEncrypt v1.0
 * @Desicrption Hmac 算法  金堡创
 */
public class HmacEncrypt {

    public static String hmacSha1Code(String content, String secretKey) {
        byte[] datas = content.getBytes(StandardCharsets.UTF_8);
        byte[] keys = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] results = HmacUtils.hmacSha1(keys, datas);
        return HexUtil.byte2Hex(results);
    }
}
