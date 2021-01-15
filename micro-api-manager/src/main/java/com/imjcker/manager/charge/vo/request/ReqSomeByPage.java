package com.imjcker.manager.charge.vo.request;

import com.lemon.common.vo.PageBase;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 10:05 2020/4/1
 * @Desicrption
 */
@Data
public class ReqSomeByPage extends PageBase implements Serializable {
    private static final long serialVersionUID = -5255458229016679841L;

    @NotNull
    private String appKey;

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;
}
