package com.imjcker.manager.elastic.model;

import lombok.Data;

@Data
public class GroupCount {
    private String sourceName;
    private long countBySourceName;
    private long countSuccessBySourceName;
    private long countFailBySourceName;
    private long countExceptionBySourceName;
    private int avgSuccessResponseTime;
}
