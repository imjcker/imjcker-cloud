package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.DatasourceBillDayDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2020/3/19
 **/
@Data
public class DatasourceBillDetailVO extends ShowVO {
    private List<DatasourceBillDayDetail> list = new ArrayList<>();
}
