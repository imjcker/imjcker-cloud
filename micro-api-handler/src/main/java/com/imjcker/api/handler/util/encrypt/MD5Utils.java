package com.imjcker.api.handler.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description : MD5 加密  公平网,  中诚信,  度小满
 * @Date : 2020/4/9 9:56
 */
public class MD5Utils {

    private static String md5(String data) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte b[] = data.getBytes("UTF8");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer sb = new StringBuffer();
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

    private static String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * @param msg
     * @return
     */
    public static String genMd5(String msg) {
        String messageDigest = null;
        try {
            messageDigest = md5(msg);
        } catch (Exception e) {
            throw new RuntimeException("Md5 Error. Cause: " + e);
        }

        return messageDigest;
    }

    /**
     * ���b2b���� MD5
     *
     * @param map
     * @return
     */
    public static String genB2bMd5(Map<String, String> map) {

        List<String> md5List = new ArrayList<String>();
        String md5Str = "";
        String separation = ":";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if ("transactionType".equalsIgnoreCase(name)
                    || "checksum".equalsIgnoreCase(name)
                    || ("multiplierFlag".equalsIgnoreCase(name) && !"1"
                    .equalsIgnoreCase(value))) {
                continue;
            }
            md5List.add(name + "=" + value);

        }
        Object[] md5Array = md5List.toArray();
        Arrays.sort(md5Array);
        for (int i = 0; i < md5Array.length; i++) {
            md5Str += md5Array[i];
            if (i != md5Array.length - 1) {
                md5Str += separation;
            }
        }
        return genMd5(md5Str);
    }
}
