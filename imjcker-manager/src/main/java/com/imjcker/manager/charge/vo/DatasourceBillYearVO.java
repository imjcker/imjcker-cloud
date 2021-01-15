package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.DatasourceBillYear;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2020/3/19
 **/
@Data
public class DatasourceBillYearVO extends ShowVO {
    private List<DatasourceBillYear> list = new ArrayList<>();
}
