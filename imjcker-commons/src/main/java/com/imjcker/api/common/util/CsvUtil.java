package com.imjcker.api.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件操作
 */
@Slf4j
public class CsvUtil {
    /**
     * 功能说明：获取UTF-8编码文本文件开头的BOM签名。
     * BOM(Byte Order Mark)，是UTF编码方案里用于标识编码的标准标记。例：接收者收到以EF BB BF开头的字节流，就知道是UTF-8编码。
     *
     * @return UTF-8编码文本文件开头的BOM签名
     */
    public static String getBOM() {

        byte b[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        return new String(b);
    }

    /**
     * 生成CVS文件
     *
     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List<LinkedHashMap<String, Object>> exportData, LinkedHashMap map, String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //定义文件名格式并创建
            csvFile = new File(outPutPath + fileName + ".csv");
            file.createNewFile();
            // UTF-8使正确读取分隔符","
            //如果生产文件乱码，windows下用gbk，linux用UTF-8
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);
            //写入前段字节流，防止乱码
            csvFileOutputStream.write(getBOM());
            // 写入文件头部，列名
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                csvFileOutputStream.write((String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            Iterator iterator = exportData.iterator();
            while (iterator.hasNext()) {
                LinkedHashMap<String, Object> row = (LinkedHashMap<String, Object>) iterator.next();//行数据
                //处理单行数据
                Iterator propertyIterator = map.entrySet().iterator();
                while (propertyIterator.hasNext()) {//列数据
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                    String str = row != null ? row.get(propertyEntry.getKey()) + "" : "";
                    if (StringUtils.isBlank(str)) {
                        str = "";
                    } else {
                        str = str.replaceAll("\"", "\"\"");
                        if (str.indexOf(",") >= 0) {
                            str = "\"" + str + "\"";
                        }
                    }
                    csvFileOutputStream.write(str);
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            log.error("csv文件导出异常:{}",e.getMessage());
        } finally {
            try {
                if (null != csvFileOutputStream)
                    csvFileOutputStream.close();
            } catch (IOException e) {
                log.error("csv文件导出资源关闭异常:{}",e.getMessage());
            }
        }
        return csvFile;
    }

}
