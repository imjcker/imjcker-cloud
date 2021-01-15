package com.imjcker.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OutputFileUtil {
    private static final Logger log = LoggerFactory.getLogger(OutputFileUtil.class);
//    public static void creatFile(String filePath, String fileName) {
//        File folder = new File(filePath);
//        //文件夹路径不存在
//        if (!folder.exists() && !folder.isDirectory()) {
//            folder.mkdirs();
//        }
//        // 如果文件不存在就创建
//        File file = new File(filePath + File.separator + fileName);
//        try {
//            boolean delete = false;
//            if (file.exists())
//                delete = file.delete();
//            if (delete)
//                file.createNewFile();
//        } catch (IOException e) {
//            log.error("发生异常:{}", e.getMessage());
//        }
//    }

    public synchronized static void createFile(String filePath) {
        String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
        File folder = new File(dir);
        if (!folder.exists()) {
            boolean mkdirs = folder.mkdirs();
            if (!mkdirs)
                log.debug("create dir [{}] failed.", dir);
        }

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile)
                    log.error("create file [{}] failed.", file);
            } catch (IOException e) {
                log.error("发生异常:{}",e.getMessage());
            }
        }
    }
}
