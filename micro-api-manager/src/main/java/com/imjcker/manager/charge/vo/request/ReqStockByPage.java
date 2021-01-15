package com.imjcker.manager.charge.vo.request;

import com.lemon.common.vo.PageBase;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 10:33 2020/4/1
 * @Desicrption
 */
@Data
public class ReqStockByPage extends PageBase implements Serializable {

    private static final long serialVersionUID = -85223151144231520L;
    @NotNull
    private String appKey;
    @NotNull
    private Integer apiId;

}
