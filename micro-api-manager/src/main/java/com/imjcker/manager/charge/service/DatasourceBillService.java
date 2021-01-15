package com.imjcker.manager.charge.service;

import com.imjcker.manager.charge.po.DatasourceBillDayQuery;
import com.imjcker.manager.charge.po.ProfitQuery;
import com.imjcker.manager.charge.vo.DatasourceBillDetailVO;
import com.imjcker.manager.charge.vo.DatasourceBillMonthVO;
import com.imjcker.manager.charge.vo.DatasourceBillYearVO;
import com.imjcker.manager.charge.vo.ShowVO;
import com.imjcker.manager.elastic.model.DatasourceChargeCount;
import com.imjcker.manager.elastic.model.SourceLogInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface DatasourceBillService {

    DatasourceBillYearVO listBillByYear(DatasourceBillDayQuery query) ;

    DatasourceBillMonthVO listBillByMonth(DatasourceBillDayQuery query) ;

    ShowVO listBillByDay(DatasourceBillDayQuery query) ;

    Map<String,BigDecimal> listBillDay(ProfitQuery query);

    DatasourceBillDetailVO listBillByDayDetail(DatasourceBillDayQuery query) ;

    Map<Integer,BigDecimal> listBillDetail(ProfitQuery query) ;

    List<SourceLogInfo> getQueryInfoListByTime(long startTime, long endTime);

    void saveDatasourceBillByYear(Long startTime, Long endTime);

    void saveDatasourceBillByMonth(Long startTime, Long endTime);

    void saveDatasourceBillByDay(Long startTime, Long endTime);

    List<DatasourceChargeCount> test(long startTime, long endTime);

    void downloadBill(String sourceName, Long startTime, Long endTime, String filePath, String fileName) throws IOException;
}
