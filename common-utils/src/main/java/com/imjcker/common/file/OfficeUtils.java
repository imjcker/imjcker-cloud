package com.imjcker.common.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class OfficeUtils {
    public static String readFromXLS2003(String filePath) {
        File excelFile;// Excel文件对象
        InputStream is = null;// 输入流对象
        StringBuilder cellStr = new StringBuilder();// 单元格，最终按字符串处理
        try {
            excelFile = new File(filePath);
            is = new FileInputStream(excelFile);// 获取文件输入流
            HSSFWorkbook workbook2003 = new HSSFWorkbook(is);// 创建Excel2003文件对象
            HSSFSheet sheet = workbook2003.getSheetAt(0);// 取出第一个工作表，索引是0
            // 开始循环遍历行，表头不处理，从1开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);// 获取行对象
                if (row == null) {// 如果为空，不处理
                    continue;
                }
                // 循环遍历单元格
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);// 获取单元格对象
                    if (cell == null)
                        continue;
                    else if (cell.getCellType() == CellType.BOOLEAN) {// 对布尔值的处理
                        cellStr.append(cell.getBooleanCellValue());
                    } else if (cell.getCellType() == CellType.NUMERIC) {// 对数字值的处理
                        cellStr.append(cell.getNumericCellValue());
                    } else {// 其余按照字符串处理
                        cellStr.append(cell.getStringCellValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cellStr.toString();
    }

    public static String readFromXLSX2007(String filePath) {
        File excelFile;// Excel文件对象
        InputStream is = null;// 输入流对象
        StringBuilder cellStr = new StringBuilder();// 单元格，最终按字符串处理
        try {
            excelFile = new File(filePath);
            is = new FileInputStream(excelFile);// 获取文件输入流
            org.apache.poi.ss.usermodel.Workbook workbook2007 = WorkbookFactory.create(is);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook2007.getSheetAt(0);
            // 开始循环遍历行，表头不处理，从1开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);// 获取行对象
                if (row == null) {// 如果为空，不处理
                    continue;
                }
                // 循环遍历单元格
                for (int j = 0; j < row.getLastCellNum(); j++) {
//                    XSSFCell cell = row.getCell(j);// 获取单元格对象
                    Cell cell = row.getCell(j);// 获取单元格对象
                    if (cell == null)
                        continue;
                    else if (cell.getCellType() == CellType.BOOLEAN) {// 对布尔值的处理
                        cellStr.append(cell.getBooleanCellValue());
                    } else if (cell.getCellType() == CellType.NUMERIC) {// 对数字值的处理
                        cellStr.append(cell.getNumericCellValue());
                    } else {// 其余按照字符串处理
                        cellStr.append(cell.getStringCellValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cellStr.toString();
    }

    public static String readTextFromExcel(String filePath) {
        StringBuilder cellStr = new StringBuilder();//单元格，最终按字符串处理
        //创建来自excel文件的输入流
        try {
            FileInputStream is = new FileInputStream(filePath);
            //创建WorkBook实例
            Workbook workbook = null;
            if (filePath.toLowerCase().endsWith("xls")) {//2003
                workbook = new HSSFWorkbook(is);
            } else if (filePath.toLowerCase().endsWith("xlsx")) {//2007
                workbook = WorkbookFactory.create(is);
            }
            //获取excel文件的sheet数量
            int numOfSheets = workbook.getNumberOfSheets();
            //挨个遍历sheet
            for (int i = 0; i < numOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //挨个遍历sheet的每一行
                for (Iterator<Row> iterRow = sheet.iterator(); iterRow.hasNext(); ) {
                    Row row = iterRow.next();
                    int j = 0;//标识位，用于标识第几列
                    //挨个遍历每一行的每一列
                    for (Iterator<Cell> cellIter = row.cellIterator(); cellIter.hasNext(); ) {
                        Cell cell = cellIter.next();//获取单元格对象
                        if (j == 0) {
                            if (cell == null) {// 单元格为空设置cellStr为空串
                                continue;
                            } else if (cell.getCellType() == CellType.BOOLEAN) {// 对布尔值的处理
                                cellStr.append(cell.getBooleanCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {// 对数字值的处理
                                cellStr.append(cell.getNumericCellValue());
                            } else {// 其余按照字符串处理
                                cellStr.append(cell.getStringCellValue());
                            }
                            j++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cellStr.toString();
    }
}
