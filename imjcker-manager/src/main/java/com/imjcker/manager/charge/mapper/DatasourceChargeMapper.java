package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.DatasourceChargeVO;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.DatasourceChargeVO;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/13
 */
public interface DatasourceChargeMapper extends TkBaseMapper<DatasourceCharge> {

    List<DatasourceChargeVO> selectForPage(DatasourceChargeVO record);
}
