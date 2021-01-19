package com.imjcker.manager.util;

import java.io.*;

/**
 * @author thh <a href="http://github.com/imjcker">TanHaihui</a>
 * @version 1.0.0
 * description: 加密解密文书文件工具类
 **/
public class DataEncryption {
    // 默认密钥
    private static byte[] KEY = new byte[]{10, 13, 39, 27, 32, 12, 63, 55};


    /**
     * 对字节做异或操作
     *
     * @param b   字节
     * @param key 密钥
     * @return 加密后的字节
     */
    private static byte ByteXor(byte b, byte key) {
        b = (byte) (b ^ key);
        return b;
    }

    /**
     * 对字节数组做异或操作
     *
     * @param data 字节数组
     * @param key  密钥
     * @return 加密后的字节数组
     */
    private static byte[] ByteXor(byte[] data, byte[] key) {
        int keyLen = key.length;
        int dataLen = data.length;
        if (dataLen == 0) {
            return data;
        }
        for (int i = 0; i < dataLen; i++) {
            data[i] = ByteXor(data[i], key[i % keyLen]);
        }
        return data;
    }

    /**
     * 加密，key必须是2的n次方
     *
     * @param data 数据
     * @param key  密钥
     * @return 加密数据
     */
    public static byte[] Encryption(byte[] data, byte[] key) {
        return ByteXor(data, key);
    }

    /**
     * 加密, 使用内置密钥
     *
     * @param data 数据
     * @return 加密数据
     */
    public static byte[] Encryption(byte[] data) {
        return ByteXor(data, KEY);
    }

    /**
     * 加密文件
     *
     * @param srcFile  明文文件
     * @param descFile 密文文件
     * @param key      密钥
     */
    public static void Encryption(String srcFile, String descFile, byte[] key) {
        int count;
        int keyLen = key.length;
        byte[] buffer = new byte[1024 * 512];

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(descFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            long len = new File(srcFile).length();
            long tempLen = 0;
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(descFile);

            while (tempLen < len) {
                count = fis.read(buffer, 0, 1024 * 512);
                tempLen += count;
                for (int i = 0; i < count; i++)
                    buffer[i] = ByteXor(buffer[i], key[i % keyLen]);
                fos.write(buffer, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加密文件, 使用内置密钥
     *
     * @param srcFile  明文文件
     * @param descFile 密文文件
     */
    public static void Encryption(String srcFile, String descFile) {
        Encryption(srcFile, descFile, KEY);
    }


    /**
     * 解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 解密数据
     */
    public static byte[] Decryption(byte[] data, byte[] key) {
        return ByteXor(data, key);
    }

    /**
     * 解密, 使用内置密钥
     *
     * @param data 加密数据
     * @return 解密数据
     */
    public static byte[] Decryption(byte[] data) {
        return ByteXor(data, KEY);
    }

    /**
     * 解密文件
     *
     * @param srcFile  密文文件
     * @param descFile 解密后的文件
     * @param key      密钥
     */
    public static void Decryption(String srcFile, String descFile, byte[] key) {
        Encryption(srcFile, descFile, key);
    }

    /**
     * 解密文件
     *
     * @param srcFile  密文文件, 使用内置密钥
     * @param descFile 解密后的文件
     */
    public static void Decryption(String srcFile, String descFile) {
        Decryption(srcFile, descFile, KEY);
    }

    /**
     * 根据文件路径判断是否加密文件
     *
     * @param srcFile 文件路径
     * @return 是否加密
     */
    public static boolean JudgeIsEncryFile(String srcFile) {
        return srcFile.lastIndexOf(".encry") > 0;
    }

    private static void checkClassVersion(File classFile)
            throws IOException {
        DataInputStream in = new DataInputStream
                (new FileInputStream(classFile));

        int magic = in.readInt();
        if (magic != 0xcafebabe) {
            System.out.println(classFile.getName() + " is not a valid class!");
        }
        int minor = in.readUnsignedShort();
        int major = in.readUnsignedShort();
        System.out.println(classFile.getName() + ": " + major + " . " + minor);
        in.close();
    }
}
