package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kjy 2020/3/30
 **/
@Data
public class ProfitVO extends ShowVO {
    private List<Profit> list = new ArrayList<>();
    private BigDecimal totalProfit;//利润
    private BigDecimal totalCost;//总支出
    private BigDecimal totalIncome;//总收入
}
