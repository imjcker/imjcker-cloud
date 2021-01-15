package com.imjcker.manager.util;

import com.imjcker.manager.config.DocumentConfigurationProperties;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.util.SpringUtils;
import com.lemon.common.vo.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年9月7日 下午2:21:23
 * @Title WordUtils 封装类
 * @Description
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Slf4j
@SuppressWarnings("all")
public class WordUtils {
    private WordUtils() {
        throw new RuntimeException("禁止实例化Word工具类.");
    }

    /**
     * @param dataList     封装导出数据的list
     * @param clazz        数据表头实体类
     * @param userId       操作人id
     * @param templateName excel模板名称
     * @param prefix       导出的excel名称前缀
     * @return Map<String   ,   Object>
     * @throws IOException
     * @Title: 创建excel文件并且上传
     */
    public static void buildAndUpload(Map<String, String> paramMap, List<List<String>> dataList, List<List<String>> responseParamList, Class clazz, String userId, String templateName, String wordName) throws IOException {
        DocumentConfigurationProperties docProperties = SpringUtils.getBean(DocumentConfigurationProperties.class);

        //模板地址
        String templatePath = docProperties.getTemplatePath() + File.separator + templateName + docProperties.getWordExtension();
        //文档生成路径
        if (!new File(docProperties.getTemporaryPath()).exists()) {
            boolean mkdirs = new File(docProperties.getTemporaryPath()).mkdirs();
            if (!mkdirs) {
                log.error("Create temporary path error: [{}]", docProperties.getTemporaryPath());
                throw new BusinessException("Create temporary path error: " + docProperties.getTemporaryPath());
            }
        }
        String docPath = docProperties.getTemporaryPath() + File.separator + wordName + docProperties.getWordExtension();

        if (!new File(templatePath).exists()) {
            log.error("模板文件不存在:{}", templatePath);
            throw new BusinessException("模板文件不存在");
        }

        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            //docx
            XWPFDocument xdoc = new XWPFDocument(POIXMLDocument.openPackage(templatePath));
            List<XWPFParagraph> list = xdoc.getParagraphs();
            for (int i = 0; i < list.size(); i++) {
                XWPFParagraph oldPara = list.get(i);
                String oldParaText = oldPara.getParagraphText();
                //基本内容替换
                for (Entry e : paramMap.entrySet()) {
                    String paramKey = (String) e.getKey();
                    String paramValue = (String) e.getValue();
                    if (oldParaText.contains(paramKey)) {
                        List<XWPFRun> runs = oldPara.getRuns();
                        String fontFamily = runs.get(0).getFontFamily();
                        String color = runs.get(0).getColor();
                        boolean isBold = runs.get(0).isBold();
                        int fontSize = runs.get(0).getFontSize();
                        //删除原来的内容
                        for (int j = runs.size() - 1; j >= 0; j--) {
                            list.get(i).removeRun(j);
                        }
                        //创建新内容
                        if (e.getKey().equals(Constant.WORKUTILS_RESULT_EXAMPLE)) {
                            String string = paramValue;
                            String[] results = string.split("\n");
                            for (int k = 0; k < results.length; k++) {
                                String str = results[k];
                                XWPFRun pRun = oldPara.createRun();
                                pRun.setText(str);
                                pRun.setFontFamily(fontFamily);
                                pRun.setFontSize(fontSize);
                                pRun.setColor(color);
                                pRun.setBold(isBold);
                                pRun.addCarriageReturn();
                                if (k != results.length - 2 && k != results.length - 1) {
                                    pRun.addTab();
                                }
                            }
                        } else {
                            oldParaText = oldParaText.replace((String) e.getKey(), (String) e.getValue());
                            XWPFRun paragraphRun = oldPara.createRun();
                            paragraphRun.setText(oldParaText);
                            paragraphRun.setFontFamily(fontFamily);
                            paragraphRun.setFontSize(fontSize);
                            paragraphRun.setColor(color);
                            paragraphRun.setBold(isBold);
                        }
                    }
                }
            }
            try {
                insertValueToTable(xdoc, dataList, responseParamList, 2, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            os = new FileOutputStream(docPath);
            xdoc.write(os);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static void createTableForRequestParam(XWPFTable xTable, XWPFDocument xdoc, List<List<String>> list) {
        String bgColor = "#F1F1F1";
        CTTbl ttbl = xTable.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblWidth.setW(new BigInteger("8600"));
        tblWidth.setType(STTblWidth.DXA);
        setCellText(xdoc, getCellHight(xTable, 0, 0), "参数标识", bgColor, 3800);
        setCellText(xdoc, getCellHight(xTable, 0, 1), "参数类型", bgColor, 3800);
        setCellText(xdoc, getCellHight(xTable, 0, 2), "是否必填", bgColor, 3800);
        setCellText(xdoc, getCellHight(xTable, 0, 3), "说明", bgColor, 3800);
        int number = 0;
        for (int i = 0; i < xTable.getNumberOfRows() - 1; i++) {
            number++;
//	            setCellText(xdoc,getCellHight(xTable,number,0),list.get(i).getParamName(),bgColor,3800);
//	            setCellText(xdoc,getCellHight(xTable,number,1),list.get(i).getType(),bgColor,3800);
//	            setCellText(xdoc,getCellHight(xTable,number,2),list.get(i).getParamMust(),bgColor,3800);
//	            setCellText(xdoc,getCellHight(xTable,number,3),list.get(i).getParamsDescription(),bgColor,3800);
        }
    }

    /**
     * @param resultList   填充数据
     * @param tableRowSize 模版表格行数 取第一个行数相等列数相等的表格填充
     * @param isDelTmpRow  是否删除模版行
     * @Description: 按模版行样式填充数据, 暂未实现特殊样式填充(如列合并)，只能用于普通样式(如段落间距 缩进 字体 对齐)
     */
    public static void insertValueToTable(XWPFDocument doc, List<List<String>> requestParamList, List<List<String>> responseParamList, int tableRowSize, boolean isDelTmpRow) throws Exception {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table = null;
        List<XWPFTableRow> rows = null;
        List<XWPFTableCell> cells = null;
        List<XWPFTableCell> tmpCells = null;//模版列
        XWPFTableRow tmpRow = null;//匹配用
        XWPFTableRow headRow = null;//匹配用
        XWPFTableCell tmpCell = null;//匹配用

        int flag = 0;
        for (int m = 0, tables = doc.getTables().size(); m < tables - 1; m++) {
            table = doc.getTables().get(m);
            rows = table.getRows();
            headRow = rows.get(0);
            tmpRow = rows.get(tableRowSize - 1);
            cells = tmpRow.getTableCells();
            tmpCells = tmpRow.getTableCells();
            //输入参数写入
            if (flag == 0) {
                for (int i = 0, len = requestParamList.size(); i < len; i++) {
                    XWPFTableRow row = table.createRow();
                    row.setHeight(tmpRow.getHeight());
                    List<String> list = requestParamList.get(i);
                    cells = row.getTableCells();
                    //插入的行会填充与表格第一行相同的列数
                    for (int k = 0, klen = cells.size(); k < klen; k++) {
                        tmpCell = tmpCells.get(k);
                        XWPFTableCell cell = cells.get(k);
                        copyTableCell(tmpCell, cell, list.get(k));
                    }
                    //继续写剩余的列
                    for (int j = cells.size(), jlen = list.size(); j < jlen; j++) {
                        tmpCell = tmpCells.get(j);
                        XWPFTableCell cell = row.addNewTableCell();
                        copyTableCell(tmpCell, cell, list.get(j));
                    }
                }
                flag += 1;
                //删除模板行
                table.removeRow(tableRowSize - 1);
            } else {//输出参数写入
                Set<String> set = new HashSet<>();
                set.add("1");
                int setSize = set.size();
                for (int i = 0, len = responseParamList.size(); i < len; i++) {
                    List<String> list = responseParamList.get(i);
                    //处理新的层级关系
                    if (StringUtils.isNotBlank(list.get(6))) {
                        set.add(list.get(5));
                        if (setSize < set.size()) {
                            XWPFTableRow row = table.createRow();
                            row.setHeight(tmpRow.getHeight());
                            String parentName = "";
                            for (List<String> s : responseParamList) {
                                if (s.get(0).equalsIgnoreCase(list.get(6))) {
                                    parentName = s.get(1) + "字段包含：";
                                    break;
                                }
                            }
                            row.getCell(0).setText(parentName);
                            for (int j = 0, cellSize = row.getTableCells().size(); j < cellSize; j++) {
                                CTTc ctTc = row.getCell(j).getCTTc();
                                //设置单元格格式
                                ctTc.setTcPr(headRow.getCell(0).getCTTc().getTcPr());
                                //合并单元格
                                if (j == 0) {
                                    ctTc.addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                                } else {
                                    ctTc.addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                                }
                            }
                            setSize = set.size();
                        }
                    }
                    XWPFTableRow row = table.createRow();
                    row.setHeight(tmpRow.getHeight());
                    cells = row.getTableCells();
                    //插入的行会填充与表格第一行相同的列数
                    for (int k = 0, klen = cells.size(); k < klen; k++) {
                        tmpCell = tmpCells.get(k);
                        XWPFTableCell cell = cells.get(k);
                        copyTableCell(tmpCell, cell, list.get(k + 1));
                    }
                }
            }
        }
    }

    private static void copyTableCell(XWPFTableCell source, XWPFTableCell target, String text) {
        //列属性
        target.getCTTc().setTcPr(source.getCTTc().getTcPr());
        // 删除目标 targetCell 所有单元格
        for (int pos = 0; pos < target.getParagraphs().size(); pos++) {
            target.removeParagraph(pos);
        }
        //添加段落
        for (XWPFParagraph sp : source.getParagraphs()) {
            XWPFParagraph targetP = target.addParagraph();
            copyParagraph(targetP, sp, text);
        }
    }

    private static void copyParagraph(XWPFParagraph target, XWPFParagraph source, String text) {
        // 设置段落样式
        target.getCTP().setPPr(source.getCTP().getPPr());
        // 添加Run标签
        for (int pos = 0; pos < target.getRuns().size(); pos++) {
            target.removeRun(pos);
        }
        for (XWPFRun s : source.getRuns()) {
            XWPFRun targetrun = target.createRun();
            CopyRun(targetrun, s, text);
        }
    }

    private static void CopyRun(XWPFRun target, XWPFRun source, String text) {
        target.getCTR().setRPr(source.getCTR().getRPr());
        // 设置文本
        target.setText(text);
    }

    public static void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell, String text) throws Exception {
        CTTc cttc2 = tmpCell.getCTTc();
        CTTcPr ctPr2 = cttc2.getTcPr();

        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        cell.setColor(tmpCell.getColor());
        //cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
        if (ctPr2.getTcW() != null) {
            ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
        }
        if (ctPr2.getVAlign() != null) {
            ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
        }
        if (cttc2.getPList().size() > 0) {
            CTP ctp = cttc2.getPList().get(0);
            if (ctp.getPPr() != null) {
                if (ctp.getPPr().getJc() != null) {
                    cttc.getPList().get(0).addNewPPr().addNewJc().setVal(ctp.getPPr().getJc().getVal());
                }
            }
        }

        if (ctPr2.getTcBorders() != null) {
            ctPr.setTcBorders(ctPr2.getTcBorders());
        }

        XWPFParagraph tmpP = tmpCell.getParagraphs().get(0);
        XWPFParagraph cellP = cell.getParagraphs().get(0);
        XWPFRun tmpR = null;
        if (tmpP.getRuns() != null && tmpP.getRuns().size() > 0) {
            tmpR = tmpP.getRuns().get(0);
        }
        XWPFRun cellR = cellP.createRun();
        cellR.setText(text);
        //复制字体信息
        if (tmpR != null) {
            cellR.setBold(tmpR.isBold());
            cellR.setItalic(tmpR.isItalic());
            cellR.setStrike(tmpR.isStrike());
            cellR.setUnderline(tmpR.getUnderline());
            cellR.setColor(tmpR.getColor());
            cellR.setTextPosition(tmpR.getTextPosition());
            if (tmpR.getFontSize() != -1) {
                cellR.setFontSize(tmpR.getFontSize());
            }
            if (tmpR.getFontFamily() != null) {
                cellR.setFontFamily(tmpR.getFontFamily());
            }
            if (tmpR.getCTR() != null) {
                if (tmpR.getCTR().isSetRPr()) {
                    CTRPr tmpRPr = tmpR.getCTR().getRPr();
                    if (tmpRPr.isSetRFonts()) {
                        CTFonts tmpFonts = tmpRPr.getRFonts();
                        CTRPr cellRPr = cellR.getCTR().isSetRPr() ? cellR.getCTR().getRPr() : cellR.getCTR().addNewRPr();
                        CTFonts cellFonts = cellRPr.isSetRFonts() ? cellRPr.getRFonts() : cellRPr.addNewRFonts();
                        cellFonts.setAscii(tmpFonts.getAscii());
                        cellFonts.setAsciiTheme(tmpFonts.getAsciiTheme());
                        cellFonts.setCs(tmpFonts.getCs());
                        cellFonts.setCstheme(tmpFonts.getCstheme());
                        cellFonts.setEastAsia(tmpFonts.getEastAsia());
                        cellFonts.setEastAsiaTheme(tmpFonts.getEastAsiaTheme());
                        cellFonts.setHAnsi(tmpFonts.getHAnsi());
                        cellFonts.setHAnsiTheme(tmpFonts.getHAnsiTheme());
                    }
                }
            }
        }
        //复制段落信息
        cellP.setAlignment(tmpP.getAlignment());
        cellP.setVerticalAlignment(tmpP.getVerticalAlignment());
        cellP.setBorderBetween(tmpP.getBorderBetween());
        cellP.setBorderBottom(tmpP.getBorderBottom());
        cellP.setBorderLeft(tmpP.getBorderLeft());
        cellP.setBorderRight(tmpP.getBorderRight());
        cellP.setBorderTop(tmpP.getBorderTop());
        cellP.setPageBreak(tmpP.isPageBreak());
        if (tmpP.getCTP() != null) {
            if (tmpP.getCTP().getPPr() != null) {
                CTPPr tmpPPr = tmpP.getCTP().getPPr();
                CTPPr cellPPr = cellP.getCTP().getPPr() != null ? cellP.getCTP().getPPr() : cellP.getCTP().addNewPPr();
                //复制段落间距信息
                CTSpacing tmpSpacing = tmpPPr.getSpacing();
                if (tmpSpacing != null) {
                    CTSpacing cellSpacing = cellPPr.getSpacing() != null ? cellPPr.getSpacing() : cellPPr.addNewSpacing();
                    if (tmpSpacing.getAfter() != null) {
                        cellSpacing.setAfter(tmpSpacing.getAfter());
                    }
                    if (tmpSpacing.getAfterAutospacing() != null) {
                        cellSpacing.setAfterAutospacing(tmpSpacing.getAfterAutospacing());
                    }
                    if (tmpSpacing.getAfterLines() != null) {
                        cellSpacing.setAfterLines(tmpSpacing.getAfterLines());
                    }
                    if (tmpSpacing.getBefore() != null) {
                        cellSpacing.setBefore(tmpSpacing.getBefore());
                    }
                    if (tmpSpacing.getBeforeAutospacing() != null) {
                        cellSpacing.setBeforeAutospacing(tmpSpacing.getBeforeAutospacing());
                    }
                    if (tmpSpacing.getBeforeLines() != null) {
                        cellSpacing.setBeforeLines(tmpSpacing.getBeforeLines());
                    }
                    if (tmpSpacing.getLine() != null) {
                        cellSpacing.setLine(tmpSpacing.getLine());
                    }
                    if (tmpSpacing.getLineRule() != null) {
                        cellSpacing.setLineRule(tmpSpacing.getLineRule());
                    }
                }
                //复制段落缩进信息
                CTInd tmpInd = tmpPPr.getInd();
                if (tmpInd != null) {
                    CTInd cellInd = cellPPr.getInd() != null ? cellPPr.getInd() : cellPPr.addNewInd();
                    if (tmpInd.getFirstLine() != null) {
                        cellInd.setFirstLine(tmpInd.getFirstLine());
                    }
                    if (tmpInd.getFirstLineChars() != null) {
                        cellInd.setFirstLineChars(tmpInd.getFirstLineChars());
                    }
                    if (tmpInd.getHanging() != null) {
                        cellInd.setHanging(tmpInd.getHanging());
                    }
                    if (tmpInd.getHangingChars() != null) {
                        cellInd.setHangingChars(tmpInd.getHangingChars());
                    }
                    if (tmpInd.getLeft() != null) {
                        cellInd.setLeft(tmpInd.getLeft());
                    }
                    if (tmpInd.getLeftChars() != null) {
                        cellInd.setLeftChars(tmpInd.getLeftChars());
                    }
                    if (tmpInd.getRight() != null) {
                        cellInd.setRight(tmpInd.getRight());
                    }
                    if (tmpInd.getRightChars() != null) {
                        cellInd.setRightChars(tmpInd.getRightChars());
                    }
                }
            }
        }
    }

    //设置表格高度
    private static XWPFTableCell getCellHight(XWPFTable xTable, int rowNomber, int cellNumber) {
        XWPFTableRow row = null;
        row = xTable.getRow(rowNomber);
        row.setHeight(100);
        XWPFTableCell cell = null;
        cell = row.getCell(cellNumber);
        return cell;
    }

    /**
     * @param xDocument
     * @param cell
     * @param text
     * @param bgcolor
     * @param width
     */
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell,
                                    String text, String bgcolor, int width) {
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        XWPFParagraph pIO = cell.addParagraph();
        cell.removeParagraph(0);
        XWPFRun rIO = pIO.createRun();
        rIO.setFontFamily("微软雅黑");
        rIO.setColor("000000");
        rIO.setFontSize(12);
        rIO.setText(text);
    }

    private static String getRootPath() {
        String classPath = WordUtils.class.getResource("/").getPath();
        String rootPath = "";
        if ("\\".equals(File.separator)) {//windows下
            rootPath = classPath.substring(1, classPath.indexOf("/classes"));
            rootPath = rootPath + "/classes/word/";
            rootPath = rootPath.replace("/", "\\");
        }
        if ("/".equals(File.separator)) {//linux下
            rootPath = "word/";
        }
        return rootPath;
    }

    /**
     * A more efficient method than 'getRootPath'.
     *
     * @return return word path
     */
    private static String rootPath() {
        StringBuilder classPath = new StringBuilder(WordUtils.class.getResource("/").getPath()).append("word/");
        if ("\\".equals(File.separator)) {//windows下
            return classPath.substring(1).replace("/", "\\");
        } else {//linux下
            return classPath.toString();
        }
    }

	/*public static void main(String[] args) throws IOException {
        System.out.println(getRootPath());
	}*/
}
