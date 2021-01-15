package com.imjcker.manager.charge.vo.request;

import com.lemon.common.vo.PageBase;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 10:05 2020/4/1
 * @Desicrption
 */
@Data
public class ReqAppkeyByPage extends PageBase implements Serializable {
    private static final long serialVersionUID = -5255458229016679840L;

    @NotNull
    private String appKey;
}
