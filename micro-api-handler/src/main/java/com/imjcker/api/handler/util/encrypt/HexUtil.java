package com.imjcker.api.handler.util.encrypt;

/**
 * @Author WT
 * @Date 8:53 2020/4/9
 * @Version HexUtil v1.0
 * @Desicrption 16进制转换相关
 */
public class HexUtil {

    public static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sb.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sb.append(Integer.toHexString(0xFF & bytes[i]));
            }

        }
        return sb.toString();
    }
}
