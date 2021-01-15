package com.imjcker.manager.manage.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author ztzh_tanhh 2019/12/18
 * api 统计model
 **/
@Data
public class AppKeyCountVO {
    private String appKey;
    private long count;
    private long countSuccess;
    private long countFail;
}
