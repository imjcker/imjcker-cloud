package com.imjcker.manager.charge.service;

import com.imjcker.manager.charge.po.ProfitQuery;
import com.imjcker.manager.charge.vo.ProfitDetailVO;
import com.imjcker.manager.charge.vo.ProfitVO;

import java.io.IOException;


public interface ProfitService {

    ProfitVO listProfit(ProfitQuery query) ;

    ProfitDetailVO listProfitDatail(ProfitQuery query);

    void downloadProfit(String appKey, Long startTime, Long endTime, String filePath, String fileName) throws IOException;
}
