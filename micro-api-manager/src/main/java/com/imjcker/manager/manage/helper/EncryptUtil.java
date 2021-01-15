package com.imjcker.manager.manage.helper;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptUtil {

	private EncryptUtil() {

	}
	/**
	 * @param str
	 *            加密明文
	 * @param algorithm
	 *            加密算法
	 * @return 加密结果字符串
	 */
	public static String encrypt(String str, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.reset();
//			messageDigest.update(str.getBytes());
			messageDigest.update(str.getBytes("UTF-8"));
			byte[] res = messageDigest.digest();
			return byte2hex(res);
		} catch (NoSuchAlgorithmException e) {
			return str;
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 将byte数组转换为16进制表示
	 *
	 * @param byteArray
	 * @return
	 */
	private static String byte2hex(byte[] byteArray) {
		StringBuilder  md5StrBuilder = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuilder.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuilder.append(Integer.toHexString(0xFF & byteArray[i]));
			}

		}
		return md5StrBuilder.toString();
	}

	public static String getSignature(String timestamp, String nonce, String token,String algorithm) {
		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		String[] arrTmp = { token, timestamp, nonce };
		Arrays.sort(arrTmp);
		StringBuilder sb = new StringBuilder();
		// 2.将三个参数字符串拼接成一个字符串进行sha1加密
		for (int i = 0; i < arrTmp.length; i++) {
			sb.append(arrTmp[i]);
		}
		return encrypt(sb.toString(),algorithm);
	}

	public static boolean validate(String signature, String timestamp, String nonce,String token,String algorithm) {
		String expectedSignature = getSignature(timestamp, nonce, token,algorithm);
		// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于
		return expectedSignature.equals(signature);
	}

	public static String encryptBASE64(String loginId,String password,String timestamp) throws UnsupportedEncodingException {
		byte[] key = new String(loginId+":"+password+":"+timestamp).getBytes("UTF-8");
		return new Base64Encoder().encode(key);
	}

	public static String decryptBASE64(String key) {
//		return new String((new Base64Encoder()).decode(key));
		return Arrays.toString(new Base64Encoder().decode(key));
	}
}
