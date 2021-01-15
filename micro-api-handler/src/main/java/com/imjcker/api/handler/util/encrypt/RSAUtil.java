package com.imjcker.api.handler.util.encrypt;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author WT
 * @Date 14:10 2020/4/9
 * @Version RSAUtil v1.0
 * @Desicrption  RSA 加密   金堡创,  驹马
 *  亿字节版本平台 加密验签方式 工具类
 */
public class RSAUtil {

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * **
     * RSA最大加密大小
     */
    private final static int MAX_ENCRYPT_BLOCK = 117;

    /**
     * **
     * RSA最大解密大小
     */
    private final static int MAX_DECRYPT_BLOCK = 128;

    /**
     * @Description : RSA 加密
     * @param source   加密明文
     * @param publicKey  签名公钥
     * @Return : java.lang.String
     * @Date : 2019/10/9 14:42
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] data = source.getBytes(StandardCharsets.UTF_8);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0,i = 0;
        byte[] cache = null;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK){
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            i++;
            out.write(cache, 0, cache.length);
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptData);
    }


    /**
     * 解密
     * @param content
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] data = Base64.decodeBase64(content.getBytes(Charsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").
                generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 返回UTF-8编码的解密信息
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0 , i = 0;
        byte[] cache = null;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK){
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else{
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * @Description : 返回数据验签  公钥验签
     * @param content   返回数据明文
     * @param sign  数据签名
     * @param publicKey  签名公钥
     * @Return : boolean
     * @Date : 2019/10/9 14:43
     */
    public static Boolean checkSign(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8) );
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception ignored) {

        }
        return false;
    }

    /**
     * 返回验签字符串
     * @param content
     * @param privateKey 私钥签名
     * @return
     * @throws Exception
     */
    public static String getSign(String content, String privateKey)throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(privateKey);
        PrivateKey pri = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(pri);
        signature.update(content.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64(signature.sign()));
    }
}
