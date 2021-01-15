package com.imjcker.manager.charge.vo;

import com.imjcker.manager.charge.po.CompanyBillMonth;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowVOCompanyBillMonth extends ShowVO{
    private List<CompanyBillMonth> list = new ArrayList<>();
}
