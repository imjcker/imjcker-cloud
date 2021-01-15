package com.imjcker.api.common.util;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author thh  2019/7/19
 * @version 1.0.0
 **/
public class SHA256 {
    private SHA256() { }

    /**
     * trans String to SHA256 string
     *
     * @param text target string
     * @return sha256 String
     * @throws NoSuchAlgorithmException algorithm error exception
     */
    public static String toSHA256(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }
}
