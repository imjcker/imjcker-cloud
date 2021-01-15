package com.imjcker.api.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
public class DownloadUtils {
    /**
     * 文件下载工具类
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void downloadFile(String filePath, String fileName, HttpServletResponse response) {
        log.info("filepath={},filename={}", filePath, fileName);
        File file = new File(filePath);
        FileInputStream fis = null;
        OutputStream outputStream = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            outputStream = new BufferedOutputStream(response.getOutputStream());
            fis = new FileInputStream(file);
            int b;
            byte[] buffer = new byte[4096];
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error("文件下载发生异常。", e);
        } finally {
            sourceRelease(fis, outputStream);
        }

    }

    /**
     * 文件下载工具类
     *
     * @param filePath 文件全路径
     */
    public static void downloadFile(String filePath, HttpServletResponse response) {
        File file = new File(filePath);
        FileInputStream fis = null;
        OutputStream outputStream = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(file.getName(), "UTF-8"));
            outputStream = new BufferedOutputStream(response.getOutputStream());
            fis = new FileInputStream(file);
            int b;
            byte[] buffer = new byte[4096];
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error("文件下载发生异常。", e);
        } finally {
            sourceRelease(fis, outputStream);
        }

    }

    private static void sourceRelease(FileInputStream fis, OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            log.error("资源释放异常:{}", e.getMessage());
        }
    }
}
