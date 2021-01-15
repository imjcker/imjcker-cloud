package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.ApiOfflineRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiOfflineRecordVO extends ApiOfflineRecord {
    private Integer pageNum;
    private Integer pageSize;
    private Long  startTime;
    private Long endTime;
    private  String apiName;
    private  String apiGroupName;


}
