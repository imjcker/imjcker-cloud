package com.imjcker.api.handler.util.encrypt;

import com.imjcker.api.common.util.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * @Description : AES 加密  遂宁社保,  金堡创
 * @Date : 2020/4/9 10:18
 */
public class AESUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);

    private static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private static final String algorithmStr = "AES/CBC/PKCS7Padding";

    /**
     * AES加密
     *
     * @param data 将要加密的内容
     * @param key  密钥
     * @return 已经加密的内容
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if ((null == data || data.length == 0) || (null == key || key.length == 0))
            return null;
        //不足16字节，补齐内容为差值
        int len = 16 - data.length % 16;
        for (int i = 0; i < len; i++) {
            byte[] bytes = {(byte) len};
            data = ArrayUtils.concat(data, bytes);
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.error("AES-ECB加密异常");
            return new byte[]{};
        }
    }

    public static byte[] encrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        byte[] encryptedText = null;
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        Cipher cipher = null;
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            LOGGER.error("发生异常:{}", e.getMessage());
        }
        System.out.println("IV：" + new String(iv));
        try {
            if (null != cipher) {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
                encryptedText = cipher.doFinal(content);
            }
        } catch (Exception e) {
            LOGGER.error("发生异常:{}", e.getMessage());
        }
        return encryptedText;
    }

    /**
     * AES解密
     *
     * @param data 将要解密的内容
     * @param key  密钥
     * @return 已经解密的内容
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.error("AES-ECB解密异常");
            return new byte[]{};
        }
    }


    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, byte[] iv) {
        byte[] encryptedText = null;
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        Cipher cipher = null;
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            LOGGER.error("发生异常:{}", e.getMessage());
        }
        System.out.println("IV：" + new String(iv));
        try {
            if (null != cipher) {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                encryptedText = cipher.doFinal(encryptedData);
            }
        } catch (Exception e) {
            LOGGER.error("发生异常:{}", e.getMessage());
        }
        return encryptedText;
    }
}
