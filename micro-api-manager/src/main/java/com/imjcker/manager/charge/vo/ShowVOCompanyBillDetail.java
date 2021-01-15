package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.CompanyBillDayDatail;
import com.imjcker.manager.charge.po.CompanyBillDay;
import com.imjcker.manager.charge.po.CompanyBillDayDatail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowVOCompanyBillDetail extends ShowVO{
    private List<CompanyBillDayDatail> list = new ArrayList<>();
}
