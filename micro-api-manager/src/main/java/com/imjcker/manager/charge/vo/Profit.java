package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kjy 2020/3/30
 **/
@Data
public class Profit{
    /*
     * 分组名称
     */
    private String groupName;

    /*
     * 分组利润
     */
    private BigDecimal profit;

    /**
     * 分组支出
     */
    private BigDecimal cost;
    /**
     * 分组收入
     */
    private BigDecimal income;
}
