package com.imjcker.manager.manage.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ztzh_tanhh 2019/12/18
 * api 统计model
 **/
@Data
public class ApiCountVO {
    private int id;
    private int apiGroupId;
    private int apiId;
    private long count;
    private long countSuccess;
    private long countFail;
    private long countExp;
    private int avgSuccessResponseTime;
    private String apiName;
    private Date date;
}
