package com.imjcker.manager.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author thh 2018-11-06
 * @version 1.0.0
 * description:
 **/
public class FileUtils {

    /**
     * 追加内容到文件，换行追加
     *
     * @param file   待追加文件
     * @param conent 追加内容
     */
    public static void appendContent(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));// true,进行追加写。
            out.write(conent + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 文件转字节数组
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
//              System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (channel != null)
                    channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
