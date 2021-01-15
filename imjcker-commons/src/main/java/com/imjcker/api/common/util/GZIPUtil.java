package com.imjcker.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP数据处理类
 */
public class GZIPUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(GZIPUtil.class);
    /**
     * gzip压缩
     * @param str
     * @return
     */
    public static byte[] gzip(String str, String charsetName){
        if(str == null || str.length() == 0)
            return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try{
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(charsetName));
            gzip.close();
        }catch(IOException e){
            LOGGER.error("数据压缩异常:{}",e.getMessage());
            return null;
        }
        return out.toByteArray();
    }
    /**
     * 解压缩
     * @param str
     * @return
     */
    public static String unGzip(byte[] aesDecrypt) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(aesDecrypt);
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n=0;
            while((n=gzip.read(buffer)) >=0){
                out.write(buffer,0,n);
            }
            return out.toString("utf-8");
        } catch (Exception e) {
            LOGGER.error("数据解压缩异常:{}",e.getMessage());
            return null;
        }
    }
}
