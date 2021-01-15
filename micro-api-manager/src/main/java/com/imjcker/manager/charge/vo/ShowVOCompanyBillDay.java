package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.CompanyBillDay;
import com.imjcker.manager.charge.po.CompanyBillDay;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowVOCompanyBillDay extends ShowVO{
    private List<CompanyBillDay> list = new ArrayList<>();
}
