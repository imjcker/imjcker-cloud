package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.vo.*;
import com.imjcker.manager.charge.mapper.DatasourceBillDayDetailMapper;
import com.imjcker.manager.charge.mapper.DatasourceBillDayMapper;
import com.imjcker.manager.charge.mapper.DatasourceBillMonthMapper;
import com.imjcker.manager.charge.mapper.DatasourceBillYearMapper;
import com.imjcker.manager.charge.service.BillingRulesService;
import com.imjcker.manager.charge.service.DatasourceBillService;
import com.imjcker.manager.charge.service.DatasourceChargeService;
import com.imjcker.manager.charge.service.RedisService;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.CsvUtil;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisUtil;
import com.imjcker.manager.elastic.elasticsearch.DatasourceBillClient;
import com.imjcker.manager.elastic.model.ApiChargeCount;
import com.imjcker.manager.elastic.model.DatasourceChargeCount;
import com.imjcker.manager.elastic.model.SourceLogInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DatasourceBillServiceImpl implements DatasourceBillService {
    @Autowired
    private DatasourceBillClient datasourceBillClient;
    @Autowired
    private DatasourceBillDayDetailMapper datasourceBillDayDetailMapper;

    @Autowired
    private DatasourceBillDayMapper datasourceBillDayMapper;

    @Autowired
    private DatasourceBillMonthMapper datasourceBillMonthMapper;

    @Autowired
    private DatasourceBillYearMapper datasourceBillYearMapper;

    @Autowired
    private BillingRulesService billingRulesService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DatasourceChargeService datasourceChargeService;

    @Value("${es.index}")
    private String index;//ES索引名称

    @Value("${es.type}")
    private String type;//ES类型名称

    /**
     * 年账单查询
     *
     * @param query
     * @return
     */
    @Override
    public DatasourceBillYearVO listBillByYear(DatasourceBillDayQuery query) {
        String appKey = query.getSourceName();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<DatasourceBillYear> result = new ArrayList<>();
        DatasourceBillYearVO showVO = new DatasourceBillYearVO();
        //查询今年账单（当查询截止时间大于今年的起始时间，查询今年的账单）
        try {
            long thisYearBeginMills = DateUtil.yearTimeInMillis(0, null);
            if (endTime > thisYearBeginMills) {
                List<DatasourceBillYear> thisYearBill = getThisYearBill(thisYearBeginMills, System.currentTimeMillis(), appKey);//年初到此刻
                if (null != thisYearBill && thisYearBill.size() > 0)
                    result.addAll(thisYearBill);
            }
            //查询往年账单（数据库，根据起始截止时间的年份查询）
            Example yearExample = new Example(DatasourceBillYear.class);
            Example.Criteria yearCriteria = yearExample.createCriteria();
            yearExample.setOrderByClause("create_time DESC");
            if (StringUtils.isNotBlank(appKey)) {
                yearCriteria.andLike("groupName", "%" + appKey + "%");
            }
            if (null != startTime && null != endTime) {
                //获取startTime所在年份的起始时间戳，endTime所在年份的截止时间戳
                yearCriteria.andBetween("createTime", DateUtil.yearTimeInMillis(0, startTime), DateUtil.yearTimeInMillis(1, endTime) - 1);
            }
            List<DatasourceBillYear> otherYearBill = datasourceBillYearMapper.selectByExample(yearExample);
            if (null != otherYearBill && otherYearBill.size() > 0)
                result.addAll(otherYearBill);
            showVO.setTotalCount(result.size());
            List<DatasourceBillYear> page = result.stream()
                    .sorted(Comparator.comparing(DatasourceBillYear::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            log.error("查询年账单出错,{}", e.getMessage());
        }
        return showVO;
    }

    /**
     * 月账单查询
     *
     * @param query
     * @return
     */
    @Override
    public DatasourceBillMonthVO listBillByMonth(DatasourceBillDayQuery query) {
        List<DatasourceBillMonth> result = new ArrayList<>();
        String groupName = query.getSourceName();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        DatasourceBillMonthVO showVO = new DatasourceBillMonthVO();
        try {
            long thisMonthMills = DateUtil.monthTimeInMillis(0, null);
            if (endTime > thisMonthMills) {//当查询截止时间超过当月起始时间，才会实时查询当月数据
                //1-查询当月数据
                result = getThisMonthBill(thisMonthMills, endTime, groupName);
            }
            //2-数据库查询其他月份
            Example example = new Example(DatasourceBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(groupName)) {
                criteria.andLike("groupName", "%" + groupName + "%");
            }
            if (null != startTime) {
                criteria.andBetween("createTime", DateUtil.monthTimeInMillis(0, startTime), DateUtil.monthTimeInMillis(1, endTime) - 1);
            }
            example.setOrderByClause("create_time DESC");
            List<DatasourceBillMonth> otherMonthBill = datasourceBillMonthMapper.selectByExample(example);
            if (null != otherMonthBill && otherMonthBill.size() > 0)
                result.addAll(otherMonthBill);
            showVO.setTotalCount(result.size());
            List<DatasourceBillMonth> page = result.stream()
                    .sorted(Comparator.comparing(DatasourceBillMonth::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            log.error("查询月账单出错,{}", e.getMessage());
        }
        return showVO;
    }

    /**
     * 获取日账单-分页
     *
     * @return
     * @throws ParseException
     */
    @Override
    public ShowVO listBillByDay(DatasourceBillDayQuery query) {
        String appKey = query.getSourceName();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<DatasourceBillDay> result = new ArrayList<>();
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        if (endTime > todayMills) {//ES查询当日数据
            List<DatasourceBillDay> todayBill = getTodayBill(todayMills, endTime, appKey);
            if (null != todayBill && todayBill.size() > 0)
                result.addAll(todayBill);
        }
        //数据库获取账单
        Example example = new Example(DatasourceBillDay.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("create_time DESC");
        if (StringUtils.isNotBlank(appKey)) {
            criteria.andLike("groupName", "%" + appKey + "%");
        }
        if (null != startTime && null != endTime) {
            //查询范围为起始日期的起始时间戳，截止日期的截止时间戳
            criteria.andBetween("createTime", DateUtil.dayTimeInMillis(0, startTime), DateUtil.dayTimeInMillis(1, endTime) - 1);
        }
        List<DatasourceBillDay> otherDayBill = datasourceBillDayMapper.selectByExample(example);
        result.addAll(otherDayBill);
        DatasourceBillDayVO showVO = new DatasourceBillDayVO();
        showVO.setTotalCount(result.size());
        List<DatasourceBillDay> page = result.stream()
                .sorted(Comparator.comparing(DatasourceBillDay::getCreateTime).reversed())// 排序
                .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        showVO.setList(page);
        return showVO;
    }

    /**
     * 获取日账单列表
     *
     * @return
     * @throws ParseException
     */
    @Override
    public Map<String, BigDecimal> listBillDay(ProfitQuery query) {
        String sourceName = query.getSourceName();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<DatasourceBillDay> resultTemp = new ArrayList<>();
        Map<String, BigDecimal> result = new TreeMap<>();
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        if (endTime > todayMills) {//ES查询当日数据
            List<DatasourceBillDay> todayBill = getTodayBill(todayMills, endTime, sourceName);
            if (null != todayBill && todayBill.size() > 0)
                resultTemp.addAll(todayBill);
        }
        //数据库获取账单
        Example example = new Example(DatasourceBillDay.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sourceName))
            criteria.andLike("groupName", "%" + sourceName + "%");
        if (null != startTime && null != endTime) {
            //查询范围为起始日期的起始时间戳，截止日期的截止时间戳
            criteria.andBetween("createTime", DateUtil.dayTimeInMillis(0, startTime), DateUtil.dayTimeInMillis(1, endTime) - 1);
        }
        List<DatasourceBillDay> otherDayBill = datasourceBillDayMapper.selectByExample(example);
        if (null != otherDayBill && otherDayBill.size() > 0)
            resultTemp.addAll(otherDayBill);
        //根据数据源分组
        Map<String, List<DatasourceBillDay>> map = resultTemp.stream().collect(
                Collectors.groupingBy(DatasourceBillDay::getGroupName));
        for (Map.Entry<String, List<DatasourceBillDay>> entry : map.entrySet()) {
            BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDay::getAmount).reduce(BigDecimal::add).get();
            result.put(entry.getKey(), amount);
        }
        return result;
    }

    /**
     * 获取账单详情
     *
     * @param query
     * @return
     */
    @Override
    public DatasourceBillDetailVO listBillByDayDetail(DatasourceBillDayQuery query) {
        DatasourceBillDetailVO showVO = new DatasourceBillDetailVO();
        List<DatasourceBillDayDetailAndBillingName> result = new ArrayList<>();
        List<DatasourceBillDayDetail> detailList = new ArrayList<>();
        try {
            String sourceName = query.getSourceName();
            String dateType = query.getDateType();
            //根据日/月/年账单分类确定详情查询时间范围
            Long startTime = getTimeByDateType(dateType, query.getStartTime(), 0);
            Long endTime = getTimeByDateType(dateType, query.getStartTime(), 1) - 1;
            if (isCurrentDayOrMonthOrYear(dateType, startTime)) {//查询当天
                detailList = getTodayDetail(DateUtil.dayTimeInMillis(0, null), query.getStartTime(), sourceName);
                List<DatasourceBillDayDetailAndBillingName> datailAndBillingNameslList = new ArrayList<>();//获取当日账单详情,含有规则名称
                if (null != detailList && detailList.size() > 0) {
                    DatasourceBillDayDetailAndBillingName dayDatailAndBillingName = new DatasourceBillDayDetailAndBillingName();
                    detailList.forEach(datail -> {
                        BeanCustomUtils.copyPropertiesIgnoreNull(datail, dayDatailAndBillingName);
                        dayDatailAndBillingName.setStrategyName(billingRulesService.selectByBillingRules(datail.getBillingRulesUuid()).getName());
                        datailAndBillingNameslList.add(dayDatailAndBillingName);
                    });
                    result.addAll(datailAndBillingNameslList);
                }
            }
            if (!(("day".equals(dateType)) && (startTime == DateUtil.dayTimeInMillis(0, null)))) {//不是当日的，都要查数据库
                List<DatasourceBillDayDetailAndBillingName> otherDayDetail = datasourceBillDayDetailMapper.selectBillDayDetailAndBillingName(sourceName, startTime, endTime);
                if (null != otherDayDetail && otherDayDetail.size() > 0)
                    result.addAll(otherDayDetail);
            }
            //详情按照apiId分组统计
            List<DatasourceBillDayDetailAndBillingName> resultList = new ArrayList<>();
            Map<Integer, List<DatasourceBillDayDetailAndBillingName>> map = result.stream().collect(
                    Collectors.groupingBy(DatasourceBillDayDetailAndBillingName::getApiId));
            for (Map.Entry<Integer, List<DatasourceBillDayDetailAndBillingName>> entry : map.entrySet()) {
                DatasourceBillDayDetailAndBillingName datasourceBillDayDetailAndBillingName = entry.getValue().get(0);
                long called = entry.getValue().stream().mapToLong(DatasourceBillDayDetail::getCount).sum();
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDayDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                datasourceBillDayDetailAndBillingName.setCount(called);
                datasourceBillDayDetailAndBillingName.setAmount(amount);
//                //余额、余量获取(TODO)
//                redisService.get(entry.getKey() + ":balance");
////                redisService.get()
                resultList.add(datasourceBillDayDetailAndBillingName);
            }
            showVO.setTotalCount(result.size());
            List<DatasourceBillDayDetail> page = resultList.stream()
                    .sorted(Comparator.comparing(DatasourceBillDayDetail::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            log.error("详情查询出错,{}", e.getMessage());
        }
        return showVO;
    }

    @Override
    public Map<Integer, BigDecimal> listBillDetail(ProfitQuery query) {
        String sourceName = query.getSourceName();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<DatasourceBillDayDetail> resultTemp = new ArrayList<>();
        Map<Integer, BigDecimal> result = new TreeMap<>();
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        if (endTime > todayMills) {//ES查询当日数据
            List<DatasourceBillDayDetail> todayBill = getTodayDetail(todayMills, endTime, sourceName);
            if (null != todayBill && todayBill.size() > 0)
                resultTemp.addAll(todayBill);
        }
        //数据库获取账单
        Example example = new Example(DatasourceBillDayDetail.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sourceName)) {
            criteria.andLike("groupName", "%" + sourceName + "%");
        }
        if (null != startTime && null != endTime)//查询范围为起始日期的起始时间戳，截止日期的截止时间戳
            criteria.andBetween("createTime", DateUtil.dayTimeInMillis(0, startTime), DateUtil.dayTimeInMillis(1, endTime) - 1);
        List<DatasourceBillDayDetail> otherDayBill = datasourceBillDayDetailMapper.selectByExample(example);
        if (null != otherDayBill && otherDayBill.size() > 0)
            resultTemp.addAll(otherDayBill);
        //根据数据源分组
        Map<Integer, List<DatasourceBillDayDetail>> map = resultTemp.stream().collect(
                Collectors.groupingBy(DatasourceBillDayDetail::getApiId));
        for (Map.Entry<Integer, List<DatasourceBillDayDetail>> entry : map.entrySet()) {
            BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDayDetail::getAmount).reduce(BigDecimal::add).get();
            result.put(entry.getKey(), amount);
        }
        return result;
    }

    /**
     * 判断是否当日、当月、当年
     *
     * @param dateType
     * @param time
     * @return
     * @throws ParseException
     */
    private boolean isCurrentDayOrMonthOrYear(String dateType, long time) throws ParseException {
        if (("day".equals(dateType)) && (time == DateUtil.dayTimeInMillis(0, null)))
            return true;
        if (("month".equals(dateType)) && (time == DateUtil.monthTimeInMillis(0, null)))
            return true;
        if (("year".equals(dateType)) && (time == DateUtil.yearTimeInMillis(0, null)))
            return true;
        return false;
    }

    /**
     * 根据传入日期类型（day/month/year），生成time对应的时间戳,num表示上下年/月/日
     *
     * @param dateType 日期类型
     * @param time
     * @param num
     * @return
     */
    private Long getTimeByDateType(String dateType, Long time, int num) {
        Long result = null;
        try {
            if ("day".equals(dateType)) {
                result = DateUtil.dayTimeInMillis(num, time);//num=0时，获取time所在日起始时间戳
            } else if ("month".equals(dateType)) {
                result = DateUtil.monthTimeInMillis(num, time);//num=1时，获取time所在月份下个月的起始时间戳
            } else {
                result = DateUtil.yearTimeInMillis(num, time);//num=-1时，获取time所在年份上一年的起始时间戳
            }
        } catch (Exception e) {
            log.error("根据日期类型获取时间戳出错,{}", e.getMessage());
        }
        return result;
    }

    /**
     * 持久化去年账单-定时任务调用
     */
    @Override
    public void saveDatasourceBillByYear(Long startTime, Long endTime) {
        try {
            if (null == startTime && null == endTime) {
                startTime = DateUtil.yearTimeInMillis(-1, null);//去年的起始时间
                endTime = DateUtil.yearTimeInMillis(0, null) - 1;//去年的截止时间
            }
            Example example = new Example(DatasourceBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<DatasourceBillMonth> datasourceBillMonthList = datasourceBillMonthMapper.selectByExample(example);
            //按数据源分组
            Map<String, List<DatasourceBillMonth>> map = datasourceBillMonthList.stream().collect(
                    Collectors.groupingBy(DatasourceBillMonth::getGroupName));
            for (Map.Entry<String, List<DatasourceBillMonth>> entry : map.entrySet()) {
                DatasourceBillYear datasourceBillYear = new DatasourceBillYear();
                String groupName = entry.getKey();
                datasourceBillYear.setGroupName(groupName);
                //调用量求和
                long called = entry.getValue().stream().collect(Collectors.summarizingLong(DatasourceBillMonth::getCalled)).getSum();

                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillMonth::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                datasourceBillYear.setCalled(called);
                datasourceBillYear.setAmount(amount);
                datasourceBillYear.setCreateTime(startTime + 10);
                //余额、余量获取(TODO)

                datasourceBillYearMapper.insert(datasourceBillYear);
            }
        } catch (ParseException e) {
            log.error("持久化去年账单出错,{}", e.getMessage());
        }
    }

    /**
     * 持久化上月账单（显示每天客户余额余量）
     */
    @Override
    public void saveDatasourceBillByMonth(Long startTime, Long endTime) {
        String todayDate = DateUtil.getTodayDate();
        try {
            if (null == startTime && null == endTime) {
                startTime = DateUtil.monthTimeInMillis(-1, null);////获取上月起始时间戳，定时任务在月初第一天
                endTime = DateUtil.dateToStampLastMill(todayDate);//获取指定日期前一天的截止时间戳，定时任务在月初第一天，因此为上月月末截止时间
            }
            //查询上月日账单数据list
            Example example = new Example(DatasourceBillDay.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<DatasourceBillDay> billDayList = datasourceBillDayMapper.selectByExample(example);
            //查询当日账单详情，生成日账单
            //根据客户分组
            Map<String, List<DatasourceBillDay>> map = billDayList.stream().collect(
                    Collectors.groupingBy(DatasourceBillDay::getGroupName));
            for (Map.Entry<String, List<DatasourceBillDay>> entry : map.entrySet()) {
                DatasourceBillMonth billMonth = new DatasourceBillMonth();
                String groupName = entry.getKey();
                billMonth.setGroupName(groupName);
                long called = entry.getValue().stream().collect(Collectors.summarizingLong(DatasourceBillDay::getCalled)).getSum();
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDay::getAmount).reduce(BigDecimal::add).get();
                billMonth.setCalled(called);
                billMonth.setAmount(amount);
                billMonth.setCreateTime(startTime + 10);
                //余额、余量获取(TODO)
                datasourceBillMonthMapper.insert(billMonth);
            }
        } catch (Exception e) {
            log.error("持久化下游月账单出错，{}", e.getMessage());
        }
    }

    /**
     * 定时任务调用入口-持久化日账单(需先持久化详情，才能持久化日账单)
     */
    @Override
    public void saveDatasourceBillByDay(Long startTime, Long endTime) {
        try {
            if (null == startTime && null == endTime) {
                startTime = Long.parseLong(DateUtil.getBeforeTimestamp(-1));//昨天起始时间戳
                endTime = Long.parseLong(DateUtil.getBeforeTimestamp(0)) - 1;//昨天截止时间戳
            }
//            String todayDate = DateUtil.getTodayDate();
            saveCustomerChargeByDetail(startTime, endTime);//持久化账单详情
            //并且获取详情list
            Example example = new Example(DatasourceBillDayDetail.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<DatasourceBillDayDetail> dayDetailList = datasourceBillDayDetailMapper.selectByExample(example);
            //查询当日账单详情，生成日账单
            //根据客户分组
            Map<String, List<DatasourceBillDayDetail>> map = dayDetailList.stream().collect(
                    Collectors.groupingBy(DatasourceBillDayDetail::getGroupName));
            for (Map.Entry<String, List<DatasourceBillDayDetail>> entry : map.entrySet()) {
                DatasourceBillDay datasourceBillDay = new DatasourceBillDay();
                String groupName = entry.getKey();
                datasourceBillDay.setGroupName(groupName);
                long called = 0L;
                for (DatasourceBillDayDetail companyBillDayDetail : entry.getValue()) {
                    long count = companyBillDayDetail.getCount();
                    called += count;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDayDetail::getAmount).reduce(BigDecimal::add).get();
                datasourceBillDay.setCalled(called);
                datasourceBillDay.setAmount(amount);
                datasourceBillDay.setCreateTime(startTime + 10);

                datasourceBillDayMapper.insert(datasourceBillDay);
            }
        } catch (Exception e) {
            log.error("持久化下游日账单出错，{}", e.getMessage());
        }
    }

    /**
     * 定时任务调用-持久化详情
     */
    public void saveCustomerChargeByDetail(long startTime, long endTime) {
        try {
            //获取当日计费基础数据
            List<DatasourceChargeCount> customerChargeCountList = datasourceBillClient.customerCount(index, type, startTime, endTime, null);
            //持久化到数据库
            customerChargeCountList.forEach(datasourceChargeCount -> {
                List<ApiChargeCount> apiChargeCountsList = datasourceChargeCount.getCustomerApiChargelistCount();
                apiChargeCountsList.forEach(chargeCount -> {
                    DatasourceBillDayDetail billDayDetail = new DatasourceBillDayDetail();
                    billDayDetail.setApiId(chargeCount.getApiId());
                    billDayDetail.setBillingRulesUuid(chargeCount.getChargeUuid());
                    billDayDetail.setCount(chargeCount.getCount());
                    billDayDetail.setPrice(chargeCount.getPrice());
                    billDayDetail.setAmount(calculateAmount(chargeCount));
                    billDayDetail.setGroupName(datasourceChargeCount.getSourceName());
                    billDayDetail.setCreateTime(startTime + 10);
                    datasourceBillDayDetailMapper.insertSelective(billDayDetail);
                });
            });
        } catch (Exception e) {
            log.error("持久化下游账单详情出错，{}", e.getMessage());
        }
    }


    //根据计费规则，计算消费额
    private BigDecimal calculateAmount(ApiChargeCount apiChargeCount) {
        BigDecimal amount;
        JSONObject charge = RedisUtil.get("chargeRule:" + apiChargeCount.getChargeUuid());
        BillingRules billingRules = new BillingRules();
        if (null == charge){
            billingRules = billingRulesService.selectByBillingRules(apiChargeCount.getChargeUuid());//查询计费规则
        }else{
            billingRules = JSON.parseObject(charge.toJSONString(),new TypeReference<BillingRules>(){});
        }
        if (null == billingRules) {
            throw new BusinessException(apiChargeCount.getChargeUuid() + ExceptionInfo.CHARGE_NOT_EXIT);
        }
        if (null != billingRules.getBillingType()) {
            // 按条计费
            if (2 == billingRules.getBillingType()) {
                amount = apiChargeCount.getPrice().multiply(BigDecimal.valueOf(apiChargeCount.getCount()));
            } else {
                //按时计费
                //计算每天平均收益
                DatasourceCharge datasourceCharge = datasourceChargeService.getChargeByApiId(apiChargeCount.getApiId());
                //根据合约起止时间计算天数
                int days = DateUtil.getDayBetween(datasourceCharge.getStartTime(), datasourceCharge.getEndTime());
                amount = apiChargeCount.getPrice().divide(BigDecimal.valueOf(days), 2, BigDecimal.ROUND_HALF_DOWN);//一天费用
            }
            return amount;
        }
        return null;
    }

    /**
     * 查询时间段内的ES全量数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<SourceLogInfo> getQueryInfoListByTime(long startTime, long endTime) {
        //获取当日计费基础数据
        List<SourceLogInfo> customerChargeCountList = datasourceBillClient.getQueryInfoList(index, type, startTime, endTime);
        //持久化到数据库
        return customerChargeCountList;
    }

    /**
     * 实时获取今年账单
     *
     * @param startYearTime
     * @param currentTime
     * @return
     */
    public List<DatasourceBillYear> getThisYearBill(long startYearTime, long currentTime, String groupName) {
        List<DatasourceBillYear> result = new ArrayList<>();
        try {
            //今年的账单一定是含当月账单
            List<DatasourceBillMonth> monthBillList = new ArrayList<>();
            long thisMonthBeginMills = DateUtil.monthTimeInMillis(0, null);
            //当月账单（实时）
            List<DatasourceBillMonth> thisMonthBillList = this.getThisMonthBill(thisMonthBeginMills, currentTime, groupName);
            if (thisMonthBillList.size() > 0)
                monthBillList.addAll(thisMonthBillList);
            //历史月份账单
            Example example = new Example(DatasourceBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startYearTime, currentTime);
            if (StringUtils.isNotBlank(groupName)) {
                criteria.andLike("groupName", "%" + groupName + "%");
            }
            List<DatasourceBillMonth> otherMonthBillList = datasourceBillMonthMapper.selectByExample(example);
            if (otherMonthBillList.size() > 0)
                monthBillList.addAll(otherMonthBillList);
            Map<String, List<DatasourceBillMonth>> map = monthBillList.stream().collect(
                    Collectors.groupingBy(DatasourceBillMonth::getGroupName));
            for (Map.Entry<String, List<DatasourceBillMonth>> entry : map.entrySet()) {
                DatasourceBillYear datasourceBillYear = new DatasourceBillYear();
                String appKeyTemp = entry.getKey();
                datasourceBillYear.setGroupName(appKeyTemp);
                long called = entry.getValue().stream().collect(Collectors.summarizingLong(DatasourceBillMonth::getCalled)).getSum();
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillMonth::getAmount).reduce(BigDecimal::add).get();
                datasourceBillYear.setCalled(called);
                datasourceBillYear.setAmount(amount);
                datasourceBillYear.setCreateTime(currentTime);
                //余额、余量获取(TODO)
                redisService.get(appKeyTemp + ":balance");
//                redisService.get()
                result.add(datasourceBillYear);
            }
        } catch (ParseException e) {
            log.error("实时获取今年账单出错:" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取当月账单,必然包含当日实时账单
     *
     * @param thisMonthBeginMills
     * @param currentTime
     * @return
     */
    public List<DatasourceBillMonth> getThisMonthBill(long thisMonthBeginMills, long currentTime, String groupName) {
        List<DatasourceBillMonth> result = new ArrayList<>();
        List<DatasourceBillDay> thisMonthBillList = new ArrayList<>();
        // 1.1-实时查询ES当日账单
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        List<DatasourceBillDay> todayBill = getTodayBill(todayMills, currentTime, groupName);
        if (null != todayBill && todayBill.size() > 0)
            thisMonthBillList.addAll(todayBill);
        //1.2-数据库查询当月（其他日）的日账单
        Example dayExample = new Example(DatasourceBillDay.class);
        Example.Criteria dayCriteria = dayExample.createCriteria();
        if (StringUtils.isNotBlank(groupName)) {
            dayCriteria.andLike("groupName", "%" + groupName + "%");
        }
        dayCriteria.andBetween("createTime", thisMonthBeginMills, currentTime);//查询当月其他天数据库存储信息
        List<DatasourceBillDay> otherDayBill = datasourceBillDayMapper.selectByExample(dayExample);
        if (null != otherDayBill && otherDayBill.size() > 0)
            thisMonthBillList.addAll(otherDayBill);
        if (thisMonthBillList.size() > 0) {
            Map<String, List<DatasourceBillDay>> map = thisMonthBillList.stream().collect(
                    Collectors.groupingBy(DatasourceBillDay::getGroupName));
            for (Map.Entry<String, List<DatasourceBillDay>> entry : map.entrySet()) {
                DatasourceBillMonth datasourceBillMonth = new DatasourceBillMonth();
                String groupNameTmp = entry.getKey();
                datasourceBillMonth.setGroupName(groupNameTmp);
                long called = 0L;
                for (DatasourceBillDay companyBillDay : entry.getValue()) {
                    long companyBillDayCalled = companyBillDay.getCalled();
                    called += companyBillDayCalled;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDay::getAmount).reduce(BigDecimal::add).get();
                datasourceBillMonth.setCalled(called);
                datasourceBillMonth.setAmount(amount);
                datasourceBillMonth.setCreateTime(currentTime);
                //余额、余量获取(TODO)
                redisService.get(groupNameTmp + ":balance");
//                redisService.get()
                result.add(datasourceBillMonth);
            }
        }
        return result;
    }

    /**
     * 获取当日账单
     *
     * @param todayMills
     * @param currentTime
     * @return
     */
    public List<DatasourceBillDay> getTodayBill(long todayMills, long currentTime, String groupName) {
        List<DatasourceBillDay> billDayList = new ArrayList<>();
        List<DatasourceBillDayDetail> companyBillDayDetailList = getTodayDetail(todayMills, currentTime, groupName);
        if (null == companyBillDayDetailList || companyBillDayDetailList.size() <= 0)
            return billDayList;
        //查询当日账单详情，生成日账单
        //根据客户分组
        Map<String, List<DatasourceBillDayDetail>> map = companyBillDayDetailList.stream().collect(
                Collectors.groupingBy(DatasourceBillDayDetail::getGroupName));
        for (Map.Entry<String, List<DatasourceBillDayDetail>> entry : map.entrySet()) {
            DatasourceBillDay datasourceBillDay = new DatasourceBillDay();
            String groupNameTmp = entry.getKey();
            datasourceBillDay.setGroupName(groupNameTmp);
            long called = entry.getValue().stream().mapToLong(DatasourceBillDayDetail::getCount).sum();
            BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(DatasourceBillDayDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            datasourceBillDay.setCalled(called);
            datasourceBillDay.setAmount(amount);
            datasourceBillDay.setCreateTime(currentTime);
            //余额、余量获取(TODO)
            redisService.get(groupNameTmp + ":balance");
//                redisService.get()
            billDayList.add(datasourceBillDay);
        }
        return billDayList;
    }

    /**
     * 获取当日账单详情
     *
     * @param todayMills
     * @param currentTime
     * @return
     */
    public List<DatasourceBillDayDetail> getTodayDetail(long todayMills, long currentTime, String sourceName) {
        //查询截止时间大于当日起始时间，表示要查询实时数据（包含当日未持久化的数据）
        if (currentTime > todayMills) {
            //查询当日账单详情
            List<DatasourceChargeCount> customerChargeCountList = datasourceBillClient.customerCount(index, type, todayMills, currentTime, sourceName);
            List<DatasourceBillDayDetail> detailList = new ArrayList<>();
            customerChargeCountList.forEach(datasourceChargeCount -> {
                List<ApiChargeCount> apiChargeCountsList = datasourceChargeCount.getCustomerApiChargelistCount();
                apiChargeCountsList.forEach(apiChargeCount -> {
                    DatasourceBillDayDetail datasourceBillDayDetail = new DatasourceBillDayDetail();
                    datasourceBillDayDetail.setApiId(apiChargeCount.getApiId());
                    datasourceBillDayDetail.setBillingRulesUuid(apiChargeCount.getChargeUuid());
                    datasourceBillDayDetail.setCount(apiChargeCount.getCount());
                    datasourceBillDayDetail.setPrice(apiChargeCount.getPrice());
                    apiChargeCount.setCount(datasourceChargeCount.getCount());
                    datasourceBillDayDetail.setAmount(calculateAmount(apiChargeCount));
                    datasourceBillDayDetail.setGroupName(datasourceChargeCount.getSourceName());
                    datasourceBillDayDetail.setCreateTime(currentTime);
                    detailList.add(datasourceBillDayDetail);
                });
            });
            return detailList;
        }
        return null;
    }

    /**
     * 根据sourceName，起止时间查询账单详情list
     *
     * @param sourceName
     * @param startTime
     * @param endTime
     * @return
     */
    public List<DatasourceBillDayDetailAndBillingName> searchBillDetailBySourceNameAndTime(String sourceName, long startTime, long endTime) {
        List<DatasourceBillDayDetailAndBillingName> result = new ArrayList<>();
        List<DatasourceBillDayDetailAndBillingName> orderList = new ArrayList<>();
        List<DatasourceBillDayDetail> detailList = new ArrayList<>();
        try {
            //查询范围包含当天，查询ES当日账单明细
            if (endTime > DateUtil.dayTimeInMillis(0, null)) {
                detailList = getTodayDetail(DateUtil.dayTimeInMillis(0, null), System.currentTimeMillis(), sourceName);
                List<DatasourceBillDayDetailAndBillingName> datailAndBillingNameslList = new ArrayList<>();//获取当日账单详情,含有规则名称
                if (null != detailList && detailList.size() > 0) {
                    DatasourceBillDayDetailAndBillingName dayDetailAndBillingName = new DatasourceBillDayDetailAndBillingName();
                    detailList.forEach(detail -> {
                        BeanCustomUtils.copyPropertiesIgnoreNull(detail, dayDetailAndBillingName);
                        dayDetailAndBillingName.setStrategyName(billingRulesService.selectByBillingRules(detail.getBillingRulesUuid()).getName());
                        datailAndBillingNameslList.add(dayDetailAndBillingName);
                    });
                    result.addAll(datailAndBillingNameslList);
                }
            }
            //查询数据库账单详情表
            List<DatasourceBillDayDetailAndBillingName> otherDayDetail = datasourceBillDayDetailMapper.selectBillDayDetailAndBillingName(sourceName, startTime, endTime);
            if (null != otherDayDetail && otherDayDetail.size() > 0)
                result.addAll(otherDayDetail);
            //排序(按照日期排序)
            orderList = result.stream()
                    .sorted(Comparator.comparing(DatasourceBillDayDetailAndBillingName::getCreateTime).reversed()).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("导出账单详情查询出错,{}", e.getMessage());
        }
        return orderList;
    }

    /**
     * 下载账单详情
     *
     * @param sourceName 数据源名称
     * @param startTime  起始时间
     * @param endTime    截止时间
     * @return
     */
    @Override
    public void downloadBill(String sourceName, Long startTime, Long endTime, String filePath, String fileName) throws IOException {
        if (StringUtils.isNotBlank(sourceName) && null == startTime || null == endTime)
            throw new BusinessException(ExceptionInfo.NOT_NULL_PARAMS);
        //根据sourceName，起止时间查询账单详情
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        List<DatasourceBillDayDetailAndBillingName> result = searchBillDetailBySourceNameAndTime(sourceName, startTime, endTime);
        if (null != result && result.size() > 0) {//统计
            long called = result.stream().mapToLong(DatasourceBillDayDetailAndBillingName::getCount).sum();
            BigDecimal amount = result.stream().filter(a -> null != a.getAmount()).map(DatasourceBillDayDetailAndBillingName::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            DatasourceBillDayDetailAndBillingName sumObject = new DatasourceBillDayDetailAndBillingName();
            sumObject.setGroupName("总计");
            sumObject.setCount(called);
            sumObject.setAmount(amount);
            sumObject.setCreateTime(System.currentTimeMillis());
            result.add(sumObject);
            result.forEach(object -> {
                LinkedHashMap<String, Object> mapResult = new LinkedHashMap<>();
                mapResult.put("1", object.getGroupName() == null ? "" : object.getGroupName());
                mapResult.put("2", object.getApiId() == null ? "" : object.getApiId());
                mapResult.put("3", object.getStrategyName() == null ? "" : object.getStrategyName());
                mapResult.put("4", object.getPrice() == null ? "" : object.getPrice());
                mapResult.put("5", object.getCount() == null ? 0 : object.getCount());
                mapResult.put("6", object.getAmount() == null ? 0 : object.getAmount());
                mapResult.put("7", DateUtil.stampToDateNoTime(object.getCreateTime()));
                list.add(mapResult);
            });
        }
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();//设置列名
        header.put("1", "数据源");
        header.put("2", "接口id");
        header.put("3", "计费规则");
        header.put("4", "单价");
        header.put("5", "调用量");
        header.put("6", "消费额");
        header.put("7", "调用时间");
        CsvUtil.createCSVFile(list, header, filePath, fileName);
    }


    @Override
    public List<DatasourceChargeCount> test(long startTime, long endTime) {
        List<DatasourceChargeCount> customerChargeCountList = datasourceBillClient.customerCount(index, type, startTime, endTime, null);
        return customerChargeCountList;
    }
}
