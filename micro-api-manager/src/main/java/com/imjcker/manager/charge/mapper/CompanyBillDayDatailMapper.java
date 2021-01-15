package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyBillDayDatail;
import com.imjcker.manager.charge.po.CompanyBillDayDatailAndBillingName;
import com.imjcker.manager.charge.po.CompanyBillDayQuery;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyBillDayDatail;
import com.imjcker.manager.charge.po.CompanyBillDayDatailAndBillingName;
import com.imjcker.manager.charge.po.CompanyBillDayQuery;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/08
 */
public interface CompanyBillDayDatailMapper extends TkBaseMapper<CompanyBillDayDatail> {

    List<CompanyBillDayDatailAndBillingName> selectBillDayDatailAndBillingNameBySome(CompanyBillDayQuery query);
}
