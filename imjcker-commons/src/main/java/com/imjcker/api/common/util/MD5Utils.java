package com.imjcker.api.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title: MD5工具类
 * @Package com.lemon.common.util
 * @author yezhiyuan
 * @date 2017年7月18日 上午10:41:41
 * @version V2.0
 */
public class MD5Utils {

    private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String genMd5(String msg) {
        String messageDigest = null;
        try {
            messageDigest = md5(msg);
        } catch (Exception e) {
            throw new RuntimeException("Md5 Error. Cause: " + e);
        }

        return messageDigest;
    }

    private static String md5(String data) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte b[] = data.getBytes("UTF8");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
            sb.append(byteToHexString(b[i]));

        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}
