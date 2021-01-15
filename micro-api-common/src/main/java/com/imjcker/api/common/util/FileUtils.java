package com.imjcker.api.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * <p>Title: FileUtils.java
 * <p>Description: 文件工具类
 * <p>Copyright: Copyright © 2017, ZL, All Rights Reserved.
 *
 * @author zl
 * @version 1.0
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        throw new RuntimeException("禁止实例化文件工具类.");
    }

    /**
     * 得到文件类型
     * @param filename 文件名
     * @return
     */
    public static String getContentType(String filename) {
        String filenameExtension = getFilenameExtension(filename);
        if(StringUtils.isNotBlank(filenameExtension)) {
            if (filenameExtension.equals("zip") || filenameExtension.equals("ZIP")) {
                return "application/zip";
            }
            if (filenameExtension.equals("BMP") || filenameExtension.equals("bmp")) {
                return "image/bmp";
            }
            if (filenameExtension.equals("GIF") || filenameExtension.equals("gif")) {
                return "image/gif";
            }
            if (filenameExtension.equals("JPEG") || filenameExtension.equals("jpeg") || filenameExtension.equals("JPG") || filenameExtension.equals("jpg")
                    || filenameExtension.equals("PNG") || filenameExtension.equals("png")) {
                return "image/jpeg";
            }
            if (filenameExtension.equals("HTML") || filenameExtension.equals("html")) {
                return "text/html";
            }
            if (filenameExtension.equals("TXT") || filenameExtension.equals("txt")) {
                return "text/plain";
            }
            if (filenameExtension.equals("VSD") || filenameExtension.equals("vsd")) {
                return "application/vnd.visio";
            }
            if (filenameExtension.equals("PPTX") || filenameExtension.equals("pptx") || filenameExtension.equals("PPT") || filenameExtension.equals("ppt")) {
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            }
            if (filenameExtension.equals("DOCX") || filenameExtension.equals("docx") || filenameExtension.equals("DOC") || filenameExtension.equals("doc")) {
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }
            if (filenameExtension.equals("XLSX") || filenameExtension.equals("xlsx") || filenameExtension.equals("XLS") || filenameExtension.equals("xls")) {
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }
            if (filenameExtension.equals("XML") || filenameExtension.equals("xml")) {
                return "text/xml";
            }
        }
        return "text/html";
    }

    /**
     * 获取后缀
     * @param filename 文件名
     * @return
     */
    private static String getFilenameExtension(String filename) {
        if (StringUtils.isNotBlank(filename)) {
            return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        }
        return filename;
    }

    public static void copy(String oldPath, String newPath) throws IOException {
        int len = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = new FileInputStream(oldPath);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            } catch (IOException ex) {
                LOGGER.error("拷贝文件时出错.", ex);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    LOGGER.error("拷贝文件后关闭流出错.", ex);
                }
            }
        } else {
            LOGGER.warn("文件不存在:{}", oldPath);
        }
    }
}
