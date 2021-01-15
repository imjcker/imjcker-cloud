package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.DatasourceCharge;
import lombok.Data;

/**
 * @author ztzh_tanhh 2020/3/18
 **/
@Data
public class DatasourceChargeVO extends DatasourceCharge {
    private int pageNum;
    private int pageSize;

    private String groupName;
    private String apiName;
    private String billingRulesName;

}
