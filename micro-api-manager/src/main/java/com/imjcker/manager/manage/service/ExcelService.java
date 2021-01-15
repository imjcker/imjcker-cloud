package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.vo.ApiValueExcelModel;

import java.io.IOException;
import java.util.List;

public interface ExcelService {

    List<ApiValueExcelModel> convertToPreviewModel(Integer id, byte[] bytes) throws IOException;

    String formatJson(String json) throws IOException;
}
