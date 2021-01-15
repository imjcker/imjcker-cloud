package com.imjcker.manager.elastic.model;

import lombok.Data;

/**
 * @author ztzh_tanhh 2019/12/18
 * api 统计model
 **/
@Data
public class ApiCount1 {
    private long count;
    private long countSuccess;
    private long countFail;
    private int avgSuccessResponseTime;
}
