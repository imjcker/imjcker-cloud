package com.imjcker.manager.elastic.model;

import lombok.Data;

import java.util.List;


@Data
public class AppKeyCount {
    private String appKey;
    private long count;
    private long countSuccess;
    private long countFail;
    private List<ApiIdCount> apiCountList;
}
