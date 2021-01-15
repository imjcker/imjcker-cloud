package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.CompanyBillYear;
import com.imjcker.manager.charge.po.CompanyBillDay;
import com.imjcker.manager.charge.po.CompanyBillYear;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowVOCompanyBillYear extends ShowVO{
    private List<CompanyBillYear> list = new ArrayList<>();
}
