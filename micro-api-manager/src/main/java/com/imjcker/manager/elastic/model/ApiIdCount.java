package com.imjcker.manager.elastic.model;

import lombok.Data;


@Data
public class ApiIdCount {
    private int apiId;
    private String apiName;
    private long count;
    private long countSuccess;
    private long countFail;
    private int avgSuccessSpendTime;
}
