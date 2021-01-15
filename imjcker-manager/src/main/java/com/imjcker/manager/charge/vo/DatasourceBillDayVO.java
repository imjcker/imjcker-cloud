package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.DatasourceBillDay;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2020/3/19
 **/
@Data
public class DatasourceBillDayVO extends ShowVO {
    private List<DatasourceBillDay> list = new ArrayList<>();
}
