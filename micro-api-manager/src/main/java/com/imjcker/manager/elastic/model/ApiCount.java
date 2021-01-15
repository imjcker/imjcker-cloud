package com.imjcker.manager.elastic.model;

import lombok.Data;

@Data
public class ApiCount {
    private long count;
    private long countSuccess;
    private long countFail;
    private long countExp;
    private int avgSuccessResponseTime;
}
