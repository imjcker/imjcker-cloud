package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kjy 2020/3/30
 **/
@Data
public class ProfitDetailVO extends ShowVO {
    private List<ProfitDatail> list = new ArrayList<>();
}
