package com.imjcker.api.common.vo;

import com.imjcker.api.common.model.SourceLogInfo;
import lombok.Data;

@Data
public class Result {
    private String result;
    private SourceLogInfo sourceLogInfo;
}
