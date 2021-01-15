package com.imjcker.manager.charge.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author qiuwen
 * @Date 15:55 2020/5/7
 * @Desicrption
 */
@Data
@ApiModel
public class ReqAuthList implements Serializable {
    private static final long serialVersionUID = 788057906415777003L;

    @ApiModelProperty(value="接口ids")
    private List<Integer> apiIds;

    @ApiModelProperty(value="客户标识",name="appKey")
    @NotEmpty
    private String appKey;

    @ApiModelProperty(value="接口调用有效起始时间",name="startTime")
    private Long startTime;

    @ApiModelProperty(value="接口调用终止时间",name="endTime")
    private Long endTime;

    @ApiModelProperty(value="计费策略uuid",name="strategyUuid")
    @NotNull
    private String strategyUuid;

    @ApiModelProperty(value="调用余量",name="stock")
    private Long stock;

    @ApiModelProperty(value="价格,按条计费时为单价，包时计费为总价格",name="price")
    @NotNull
    @Min(value = 0)
    private BigDecimal price;
}
