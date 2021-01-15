package com.imjcker.manager.charge.service;

import com.imjcker.manager.charge.po.CompanyBillDayQuery;
import com.imjcker.manager.charge.po.ProfitQuery;
import com.imjcker.manager.charge.vo.ShowVO;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillDetail;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillMonth;
import com.imjcker.manager.charge.vo.ShowVOCompanyBillYear;
import com.imjcker.manager.elastic.model.QueryInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface CustomerBillService {

    ShowVOCompanyBillYear listBillByYear(CompanyBillDayQuery query) ;

    ShowVOCompanyBillMonth listBillByMonth(CompanyBillDayQuery query) ;

    ShowVO listBillByDay(CompanyBillDayQuery query) ;

    Map<String, BigDecimal> listBillDatail(ProfitQuery query) ;

    Map<Integer, BigDecimal> listBillApiDatail(ProfitQuery query) ;

    ShowVOCompanyBillDetail listBillByDayDatail(CompanyBillDayQuery query) ;

    List<QueryInfo> getQueryInfoListByTime(long startTime, long endTime);

    void saveCustomerChargeByYear(Long startTime,Long endTime);

    void saveCustomerChargeByMonth(Long startTime,Long endTime);

    void saveCustomerChargeByDay(Long startTime,Long endTime);


    void downloadBill(String appKey, Long startTime, Long endTime,String filePath,String fileName) throws IOException;
}
