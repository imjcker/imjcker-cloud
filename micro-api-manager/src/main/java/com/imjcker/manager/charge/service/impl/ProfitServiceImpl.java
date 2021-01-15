package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.po.ProfitQuery;
import com.imjcker.manager.charge.vo.Profit;
import com.imjcker.manager.charge.vo.ProfitDatail;
import com.imjcker.manager.charge.vo.ProfitDetailVO;
import com.imjcker.manager.charge.vo.ProfitVO;
import com.imjcker.manager.charge.service.CustomerBillService;
import com.imjcker.manager.charge.service.DatasourceBillService;
import com.imjcker.manager.charge.service.ProfitService;
import com.imjcker.manager.charge.service.WebBackendService;
import com.lemon.common.util.CsvUtil;
import com.lemon.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private DatasourceBillService datasourceBillService;

    @Autowired
    private CustomerBillService customerBillService;

    @Autowired
    private WebBackendService webBackendService;

    /**
     * 分组利润
     */
    @Override
    public ProfitVO listProfit(ProfitQuery query) {
        //根据时间段获取上游分组消费额
        Map<String, BigDecimal> datasourceBillMap = datasourceBillService.listBillDay(query);
        //获取下游相同分组下接口消费额的和
        Map<String, BigDecimal> customerBillMap = customerBillService.listBillDatail(query);
        List<Profit> result = new ArrayList<>();
        if (!customerBillMap.isEmpty()) {
            customerBillMap.forEach((customerKey, customerValue) -> {//相同分组下，客户端收入减去数据源支出，得到的差额即使利润
                if (datasourceBillMap.containsKey(customerKey)) {
                    Profit profit = new Profit();
                    profit.setGroupName(customerKey);
                    profit.setCost(datasourceBillMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillMap.get(customerKey));
                    profit.setIncome(customerValue);
                    profit.setProfit(customerValue.subtract(datasourceBillMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillMap.get(customerKey)));
                    result.add(profit);
                } else {
                    Profit profit = new Profit();
                    profit.setGroupName(customerKey);
                    profit.setCost(BigDecimal.ZERO);
                    profit.setIncome(customerValue);
                    profit.setProfit(customerValue);
                    result.add(profit);
                }
            });
        }
        if (!datasourceBillMap.isEmpty()) {
            datasourceBillMap.forEach((datasourceKey, datasourceValue) -> {
                //分组只存在于数据源，客户端不存在，默认值为0
                if (!customerBillMap.containsKey(datasourceKey)) {
                    Profit profit = new Profit();
                    profit.setGroupName(datasourceKey);
                    profit.setCost(datasourceValue);
                    profit.setIncome(BigDecimal.ZERO);
                    profit.setProfit(BigDecimal.ZERO.subtract(datasourceValue));
                    result.add(profit);
                }
            });
        }
        BigDecimal totalProfit = result.stream().map(Profit::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);//总利润
        BigDecimal totalCost = result.stream().map(Profit::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);//总支出
        BigDecimal totalIncome = result.stream().map(Profit::getIncome).reduce(BigDecimal.ZERO, BigDecimal::add);//总收入
        //分页-排序
        ProfitVO profitVO = new ProfitVO();
        profitVO.setTotalCount(result.size());
        List<Profit> profitList = result.stream()
                .sorted(Comparator.comparing(Profit::getGroupName).reversed())// 排序
                .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        profitVO.setList(profitList);
        profitVO.setTotalProfit(totalProfit);
        profitVO.setTotalCost(totalCost);
        profitVO.setTotalIncome(totalIncome);
        return profitVO;
    }

    /**
     * 接口利润详情
     *
     * @param query
     * @return
     */
    @Override
    public ProfitDetailVO listProfitDatail(ProfitQuery query) {
        String sourceName = query.getSourceName();
        //根据时间段和分组名称获取分组下接口对应的消费额
        Map<Integer, BigDecimal> datasourceBillDetailMap = datasourceBillService.listBillDetail(query);
        //根据时间段和分组名称获取分组下接口下游的收入额
        Map<Integer, BigDecimal> customerBillDetailMap = customerBillService.listBillApiDatail(query);
        List<ProfitDatail> result = new ArrayList<>();
        if (!customerBillDetailMap.isEmpty()) {
            customerBillDetailMap.forEach((customerKey, customerValue) -> {//相同分组下，客户端收入减去数据源支出，得到的差额即使利润
                if (datasourceBillDetailMap.containsKey(customerKey)) {
                    ProfitDatail profitDatail = new ProfitDatail();
                    profitDatail.setApiId(customerKey);
                    profitDatail.setCost(datasourceBillDetailMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillDetailMap.get(customerKey));
                    profitDatail.setIncome(customerValue);
                    profitDatail.setProfit(customerValue.subtract(datasourceBillDetailMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillDetailMap.get(customerKey)));
                    profitDatail.setApiName(webBackendService.getApiNameByApiId(new JSONObject().fluentPut("apiId", profitDatail.getApiId())));
                    result.add(profitDatail);
                } else {
                    ProfitDatail profit = new ProfitDatail();
                    profit.setApiId(customerKey);
                    profit.setCost(BigDecimal.ZERO);
                    profit.setIncome(customerValue);
                    profit.setProfit(customerValue);
                    profit.setApiName(webBackendService.getApiNameByApiId(new JSONObject().fluentPut("apiId", profit.getApiId())));
                    result.add(profit);
                }
            });
        }
        if (!datasourceBillDetailMap.isEmpty()) {
            datasourceBillDetailMap.forEach((datasourceKey, datasourceValue) -> {
                //分组只存在于数据源，客户端不存在，默认值为0
                if (!customerBillDetailMap.containsKey(datasourceKey)) {
                    ProfitDatail profitDatail = new ProfitDatail();
                    profitDatail.setApiId(datasourceKey);
                    profitDatail.setApiName(webBackendService.getApiNameByApiId(new JSONObject().fluentPut("apiId", profitDatail.getApiId())));
                    profitDatail.setCost(datasourceValue);
                    profitDatail.setIncome(BigDecimal.ZERO);
                    profitDatail.setProfit(BigDecimal.ZERO.subtract(datasourceValue));
                    result.add(profitDatail);
                }
            });
        }
        //分页-排序
        ProfitDetailVO showVO = new ProfitDetailVO();
        showVO.setTotalCount(result.size());
        List<ProfitDatail> page = result.stream()
                .sorted(Comparator.comparing(ProfitDatail::getApiId).reversed())// 排序
                .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        showVO.setList(page);
        return showVO;
    }

    /**
     * 分组利润list
     *
     * @param query
     * @return
     */
    public List<Profit> listProfitList(ProfitQuery query) {
        //根据时间段获取上游分组消费额
        Map<String, BigDecimal> datasourceBillMap = datasourceBillService.listBillDay(query);
        //获取下游相同分组下接口消费额的和
        Map<String, BigDecimal> customerBillMap = customerBillService.listBillDatail(query);
        List<Profit> result = new ArrayList<>();
        if (!customerBillMap.isEmpty()) {
            customerBillMap.forEach((customerKey, customerValue) -> {//相同分组下，客户端收入减去数据源支出，得到的差额即使利润
                if (datasourceBillMap.containsKey(customerKey)) {
                    Profit profit = new Profit();
                    profit.setGroupName(customerKey);
                    profit.setCost(datasourceBillMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillMap.get(customerKey));
                    profit.setIncome(customerValue);
                    profit.setProfit(customerValue.subtract(datasourceBillMap.get(customerKey) == null ? BigDecimal.ZERO : datasourceBillMap.get(customerKey)));
                    result.add(profit);
                } else {
                    Profit profit = new Profit();
                    profit.setGroupName(customerKey);
                    profit.setCost(BigDecimal.ZERO );
                    profit.setIncome(customerValue);
                    profit.setProfit(customerValue);
                    result.add(profit);
                }
            });
        }
        if (!datasourceBillMap.isEmpty()) {
            datasourceBillMap.forEach((datasourceKey, datasourceValue) -> {
                //分组只存在于数据源，客户端不存在，默认值为0
                if (!customerBillMap.containsKey(datasourceKey)) {
                    Profit profit = new Profit();
                    profit.setGroupName(datasourceKey);
                    profit.setCost(datasourceValue);
                    profit.setIncome(BigDecimal.ZERO);
                    profit.setProfit(BigDecimal.ZERO.subtract(datasourceValue));
                    result.add(profit);
                }
            });
        }
        return result;
    }

    /**
     * 利润导出
     *
     * @param
     * @param startTime
     * @param endTime
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    @Override
    public void downloadProfit(String sourceName, Long startTime, Long endTime, String filePath, String fileName) throws IOException {
        ProfitQuery query = new ProfitQuery(sourceName, startTime, endTime);
        List<Profit> result = listProfitList(query);
        BigDecimal totalCost = result.stream().map(Profit::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = result.stream().map(Profit::getIncome).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = result.stream().map(Profit::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        result.forEach(object -> {
            LinkedHashMap<String, Object> mapResult = new LinkedHashMap<>();
            mapResult.put("1", object.getGroupName() == null ? "" : object.getGroupName());
            mapResult.put("2", object.getCost() == null ? "" : object.getCost());
            mapResult.put("3", object.getIncome() == null ? "" : object.getIncome());
            mapResult.put("4", object.getProfit() == null ? "" : object.getProfit());
            mapResult.put("5", DateUtil.stampToDateNoTime(startTime));
            mapResult.put("6", DateUtil.stampToDateNoTime(endTime));
            list.add(mapResult);
        });
        LinkedHashMap<String, Object> mapResult = new LinkedHashMap<>();
        mapResult.put("1", "总额");
        mapResult.put("2", totalCost);
        mapResult.put("3", totalIncome);
        mapResult.put("4", totalProfit);
        mapResult.put("5", DateUtil.stampToDateNoTime(startTime));
        mapResult.put("6", DateUtil.stampToDateNoTime(endTime));
        list.add(mapResult);
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();//设置列名
        header.put("1", "数据源");
        header.put("2", "支出");
        header.put("3", "收入");
        header.put("4", "利润");
        header.put("5", "起始时间");
        header.put("6", "结束时间");
        CsvUtil.createCSVFile(list, header, filePath, fileName);
    }
}
