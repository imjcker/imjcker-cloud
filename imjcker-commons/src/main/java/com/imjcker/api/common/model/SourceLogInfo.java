package com.imjcker.api.common.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 存储请求数据到ES，用于记录请求情况
 */
@SuperBuilder
@Data
public class SourceLogInfo extends CommonLogInfo  {
    private  String sourceName;
    private  String sourceUrl;
    private  long sourceCreateTime;
    private  String sourceReqParam;
    private  String sourceResult;
    private  long sourceTimeout;
    private  long sourceSpendTime;
    private  Integer sourceResponseCode;
}
