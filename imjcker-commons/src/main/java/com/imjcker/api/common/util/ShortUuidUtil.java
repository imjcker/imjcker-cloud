package com.imjcker.api.common.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * @Title: 短八位UUID生成类
 * @Package com.lemon.common.util
 * @author yezhiyuan
 * @date 2017年7月12日 上午11:05:26
 * @version V2.0
 */
public class ShortUuidUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

	public static String generateShortUuid() {
	    StringBuffer shortBuffer = new StringBuffer();
	    String uuid = UUID.randomUUID().toString().replace("-", "");
	    for (int i = 0; i < 8; i++) {
	        String str = uuid.substring(i * 4, i * 4 + 4);
	        int x = Integer.parseInt(str, 16);
	        shortBuffer.append(chars[x % 0x3E]);
	    }
	    return shortBuffer.toString();
	}

	public static String getVersionId() {
		return StringUtils.join(DateUtil.getToday().replace("-", ""),"_",generateShortUuid());
	}
/*

	public static void main(String[] args) {
		System.out.println(generateShortUuid());
	}
*/

}
