package com.imjcker.manager.charge.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author qiuwen
 * @Date 17:43 2020/5/9
 * @Desicrption
 */
@Data
@ApiModel
public class ReqCheckUpdateDatasource implements Serializable {
    private static final long serialVersionUID = 5125070879255122256L;

    @ApiModelProperty(value="id",name="id")
    private Integer id;

    @ApiModelProperty(value="计费规则ID",name="billingRulesUuid")
    private String billingRulesUuid;

    @ApiModelProperty(value="单价",name="price")
    private BigDecimal price;

    @ApiModelProperty(value="起始时间",name="startTime")
    private Long startTime;

    @ApiModelProperty(value="结束时间",name="endTime")
    private Long endTime;

    @ApiModelProperty(value="总量",name="stock")
    private Integer stock;
}
