package com.imjcker.manager.elastic.model;
import lombok.Data;

import java.util.List;

@Data
public class DatasourceChargeCount {
    /*
    数据源
     */
    private String sourceName;
    /*
    调用量
     */
    private long count;
    /*
     * 详情list
     */
    private List<ApiChargeCount> customerApiChargelistCount;
}
