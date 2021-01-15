package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/09
 */
public interface CompanyAppsRechargeMapper extends TkBaseMapper<CompanyAppsRecharge> {

    List<CompanyAppsRecharge> queryChargeRecord(@Param("appKey") String appKey);
}
