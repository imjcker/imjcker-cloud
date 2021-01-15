package com.imjcker.api.handler.util.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author WT
 * @Date 8:49 2020/4/9
 * @Version SHAEncrypt v1.0
 * @Desicrption SHA 系列算法  房管局  四川国税
 */
public class SHAEncrypt {

    public static String encrypt(String source, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(source.getBytes(StandardCharsets.UTF_8));
            byte[] res = messageDigest.digest();
            return HexUtil.byte2Hex(res);
        } catch (NoSuchAlgorithmException e) {
            return source;
        }
    }
}
