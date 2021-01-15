package com.imjcker.manager.charge.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 16:51 2020/3/31
 * @Desicrption
 */
@Data
@ApiModel
public class RespBillingRules implements Serializable {
    private static final long serialVersionUID = 3060368011070635883L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "规则唯一uid")
    private String uuid;

    @ApiModelProperty(value = "规则名称")
    private String name;

    @ApiModelProperty(value = "计费类型")
    private Integer billingType;

    @ApiModelProperty(value = "计费周期 1-年，2-季度，3-月")
    private Integer billingCycle;

    @ApiModelProperty(value = " 周期内最大次数")
    private Long billingCycleLimit;

    @ApiModelProperty(value = "计费模式")
    private Integer billingMode;

    @ApiModelProperty(value = "规则创建时间")
    private Long createTime;

    @ApiModelProperty(value = "规则更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "规则是否可用，1可用，0不可用")
    private Integer status;
}
