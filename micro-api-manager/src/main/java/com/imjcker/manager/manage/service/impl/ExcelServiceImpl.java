package com.imjcker.manager.manage.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjcker.manager.manage.vo.ApiValueExcelModel;
import com.imjcker.manager.manage.vo.ValueModel;
import com.lemon.common.exception.vo.DataValidationException;
import com.imjcker.manager.manage.service.ApiService;
import com.imjcker.manager.manage.service.ExcelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ApiService apiService;

    private static final int FIRST_ROW_WITH_DEFINITION = 1;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ApiValueExcelModel> convertToPreviewModel(Integer id, byte[] bytes) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(bytes);

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();

        List<ApiValueExcelModel> excelResponses = new ArrayList<>();

        List<ValueModel> valueModels = new ArrayList<>();

        for (int rowIndex = FIRST_ROW_WITH_DEFINITION; rowIndex <= rows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || isRowEmpty(row)) {
                continue;
            }

            String level = getStringCellValue(row, 0);
            String value = getStringCellValue(row, 3);
            String mapping = getStringCellValue(row, 4);
            String description = getStringCellValue(row, 5);
            String valueMapping = "";
            if (StringUtils.isNotBlank(mapping) && StringUtils.isNotBlank(description)) {
                valueMapping = mapping.concat(",").concat(description);
            }


            String key = getStringCellValue(row, 1);


            // 抽取值key,抽取value,value映射 同时为空时 这条记录无效
            if (StringUtils.isBlank(key) && valueIsEmpty(value) && valueMappingIsEmpty(valueMapping)) {
               continue;
            }

            String transKey = getStringCellValue(row, 2);
            // 抽取值key 和 key映射同时不为空或者同时不为空时 才取value 和value映射
            if ((StringUtils.isNotBlank(key) && StringUtils.isNotBlank(transKey))
                    || (keyIsEmpty(key) && keyMappingIsEmpty(transKey))) {

                // value和value映射当中只有一个为空验证不通过
                if (valueIsEmpty(value) && !valueMappingIsEmpty(valueMapping)) {
                    throw new DataValidationException("value 和 value映射中有一个为空");
                }
                if (!valueIsEmpty(value) && valueMappingIsEmpty(valueMapping)) {
                    throw new DataValidationException("value 和 value映射中有一个为空");
                }

                valueModels.add(buildValueModel(value, valueMapping));
            }

            if (StringUtils.isNotBlank(level)) {
                excelResponses.add(buildExcelModel(level, key, transKey, valueModels));
                valueModels = new ArrayList<>();
            }

        }

        apiService.updateApiResponseTransParam(id, objectMapper.writeValueAsString(excelResponses));
        return excelResponses;
    }

    @Override
    public String formatJson(String json) throws IOException {
        Object obj = objectMapper.readValue(json, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    private boolean isRowEmpty(Row row) {
        for (int c = 1; c <= 5; c++) {
            Cell cell = row.getCell(c);
            if (cell != null && StringUtils.isNotBlank(cell.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    private String getStringCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return StringUtils.EMPTY;
        }
        return cell.getStringCellValue();
    }

    // key映射是否为空
    private boolean keyMappingIsEmpty(String keyMapping) {

        return StringUtils.isBlank(keyMapping);
    }

    // 抽取值key是否为空
    private boolean keyIsEmpty(String key) {

        return StringUtils.isBlank(key);
    }

    private boolean valueIsEmpty(String value) {

        return StringUtils.isBlank(value);
    }

    private boolean valueMappingIsEmpty(String valueMapping) {

        return StringUtils.isBlank(valueMapping);
    }

    private ApiValueExcelModel buildExcelModel(String deep, String key, String transKey, List<ValueModel> valueModels) {
        ApiValueExcelModel excelModel = new ApiValueExcelModel();
        excelModel.setDeep(deep);
        excelModel.setType(StringUtils.EMPTY);
        excelModel.setIsData(StringUtils.EMPTY);
        excelModel.setKey(key);
        excelModel.setTransKey(transKey);
        excelModel.setOption(valueModels);
        return excelModel;
    }

    private ValueModel buildValueModel(String value, String valueMapping) {
        ValueModel valueModel = new ValueModel();
        valueModel.setOrign(value);
        valueModel.setTarget(valueMapping);
        return valueModel;
    }

}
