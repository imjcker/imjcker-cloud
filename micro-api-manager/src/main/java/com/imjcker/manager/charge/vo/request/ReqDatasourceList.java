package com.imjcker.manager.charge.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author qiuwen
 * @Date 14:44 2020/5/7
 * @Desicrption
 */
@Data
@ApiModel
public class ReqDatasourceList implements Serializable {
    private static final long serialVersionUID = -1480966418217368309L;

    @ApiModelProperty(value = "接口id集合")
    private List<Integer> apiIds;

    @ApiModelProperty(value="数据源ID",name="groupId")
    @NotNull(message = "groupId不为空")
    private Integer groupId;

    @ApiModelProperty(value="计费规则ID",name="billingRulesUuid")
    private String billingRulesUuid;

    @ApiModelProperty(value="单价",name="price")
    @NotNull(message = "价格不为空")
    @Min(value = 0)
    private BigDecimal price;

    @ApiModelProperty(value="总量",name="stock")
    private Integer stock;

    @ApiModelProperty(value="起始时间",name="startTime")
    private Long startTime;

    @ApiModelProperty(value="终止时间",name="endTime")
    private Long endTime;

}
