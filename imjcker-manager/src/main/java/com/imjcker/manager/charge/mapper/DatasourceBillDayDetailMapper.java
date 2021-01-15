package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.DatasourceBillDayDetail;
import com.imjcker.manager.charge.po.DatasourceBillDayDetailAndBillingName;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.DatasourceBillDayDetail;
import com.imjcker.manager.charge.po.DatasourceBillDayDetailAndBillingName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/13
 */
public interface DatasourceBillDayDetailMapper extends TkBaseMapper<DatasourceBillDayDetail> {

    List<DatasourceBillDayDetailAndBillingName> selectBillDayDetailAndBillingName(@Param("sourceName") String sourceName, @Param("startTime") long startTime, @Param("endTime") long endTime);

}
