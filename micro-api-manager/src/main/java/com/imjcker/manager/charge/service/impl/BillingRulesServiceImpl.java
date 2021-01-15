package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.mapper.BillingRulesMapper;
import com.imjcker.manager.charge.model.PageInfo;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.service.*;
import com.imjcker.manager.charge.service.*;
import com.imjcker.manager.charge.utils.ConstantEnum;
import com.imjcker.manager.charge.vo.response.RespBillingRules;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.UUIDUtil;
import com.lemon.common.vo.CommonStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@Transactional(rollbackFor = Exception.class)
public class BillingRulesServiceImpl implements BillingRulesService {
    @Autowired
    private BillingRulesMapper billingRulesMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DatasourceChargeService datasourceChargeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerRechargeService customerRechargeService;


    @Override
    public void delete(Integer id) {
        //编辑之前先校验原始规则是否被绑定
        BillingRules billingRules = billingRulesMapper.selectById(id);
        if (!isChargeUuidUsed(billingRules.getUuid()))//规则正在被使用，不允许删除
            throw new DataValidationException(ExceptionInfo.ALREADY_IN_USE);
        billingRulesMapper.deleteById(id);
        redisService.delete("chargeRule:" + billingRules.getUuid());
    }

    @Override
    public Map<String, Object> list(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        String name = jsonObject.getString("name");
        Integer billingMode = jsonObject.getInteger("billingMode");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        PageInfo pageInfo = new PageInfo(pageNum, pageSize);
        if (StringUtils.isBlank(name) && null == billingMode) {//查询所有
            return this.show(pageInfo);
        }
        //搜索
        List<BillingRules> billingRulesList = billingRulesMapper.findByKeyword(name, billingMode, pageNum, pageSize);//查数据库所有记录,模糊查询所有ip前缀的数据
        int totalCount = billingRulesMapper.getTotalCountByKeyword(name, billingMode);
        pageInfo.setTotalCount(totalCount);
        map.put("list", billingRulesList);
        map.put("pageNum", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }

    @Override
    public boolean save(JSONObject jsonObject) {
        //参数为空校验
        checkParam(jsonObject);
        //校验是否存在
        BillingRules billingRules = jsonObject.toJavaObject(BillingRules.class);
        if (null == billingRules.getBillingCycleLimit())//周期额度为null，表示不限次数，值为-1
            billingRules.setBillingCycleLimit(-1L);
        if (2 == billingRules.getBillingType()) {//按条计费，不要要限定时间和量
            billingRules.setBillingCycle(null);
            billingRules.setBillingCycleLimit(null);
        }
        isExist(billingRules);
        billingRules.setName(getChargeUuidName(billingRules));
        billingRules.setUpdateTime(System.currentTimeMillis());
        billingRules.setCreateTime(System.currentTimeMillis());
        billingRules.setUuid(UUIDUtil.creatUUID());
        billingRules.setStatus(CommonStatus.ENABLE);
        billingRulesMapper.insertSelective(billingRules);
        redisService.set("chargeRule:" + billingRules.getUuid(), billingRules);
        return true;
    }

    /**
     * 根据计费规则自动生成计费名称
     *
     * @param billingRules
     * @return
     */
    private String getChargeUuidName(BillingRules billingRules) {
        Integer billingType = billingRules.getBillingType();
        Integer billingCycle = billingRules.getBillingCycle();
        Long billingCycleLimit = billingRules.getBillingCycleLimit();
        Integer billingMode = billingRules.getBillingMode();
        StringBuilder stringBuilder = new StringBuilder();
        if (billingMode.intValue() == ConstantEnum.BillingMode.billingCheck.getValue()) {
            stringBuilder.append(ConstantEnum.BillingMode.billingCheck.getName());//查得
        } else {
            stringBuilder.append(ConstantEnum.BillingMode.billingEnquery.getName());//查询
        }
        stringBuilder.append("-");
        if (billingType.intValue() == ConstantEnum.BillingType.billingFees.getValue()) {
            stringBuilder.append(ConstantEnum.BillingType.billingFees.getName());//按条计费
            return stringBuilder.toString();
        } else {
            //时间计费
            if (billingCycle.intValue() == ConstantEnum.BillingCycle.year.getValue()) {
                stringBuilder.append(ConstantEnum.BillingCycle.year.getName());//包年
            } else if (billingCycle.intValue() == ConstantEnum.BillingCycle.quarter.getValue()) {
                stringBuilder.append(ConstantEnum.BillingCycle.quarter.getName());//包季度
            } else {
                stringBuilder.append(ConstantEnum.BillingCycle.month.getName());//包月
            }
            //量判断
            if (billingCycleLimit.intValue() == ConstantEnum.BillingCycleLimit.billingEnquery.getValue()) {
                stringBuilder.append(ConstantEnum.BillingCycleLimit.billingEnquery.getName());
            } else {
                stringBuilder.append(billingCycleLimit + "条");
            }
            return stringBuilder.toString();
        }
    }

    /**
     * 验证是否存在
     *
     * @param billingRules
     */
    private void isExist(BillingRules billingRules) {
        Integer billingType = billingRules.getBillingType();
        Integer billingCycle = billingRules.getBillingCycle();
        Long billingCycleLimit = billingRules.getBillingCycleLimit();
        Integer billingMode = billingRules.getBillingMode();
        List<BillingRules> billingRulesList = new ArrayList<>();
        if (2 == billingType)
            billingRulesList = billingRulesMapper.findRule(billingType, billingMode);
        else
            billingRulesList = billingRulesMapper.find(billingType, billingCycle, billingCycleLimit, billingMode);//精确查询数据库所有记录
        if (null != billingRulesList && billingRulesList.size() > 0)
            throw new DataValidationException(billingRulesList.get(0).getName() + ExceptionInfo.EXIST);
    }

    /**
     * 后端校验新增规则参数是否齐全
     *
     * @param jsonObject
     */
    private void checkParam(JSONObject jsonObject) {
        Integer billingType = jsonObject.getInteger("billingType");
        Integer billingCycle = jsonObject.getInteger("billingCycle");
        Long billingCycleLimit = jsonObject.getLong("billingCycleLimit");
        Integer billingMode = jsonObject.getInteger("billingMode");
        if (null == billingType || null == billingMode)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        if ((1 == billingType && null == billingCycle)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
    }

    @Override
    public boolean edit(JSONObject jsonObject) {
        checkParam(jsonObject);
        //校验是否存在
        BillingRules billingRules = jsonObject.toJavaObject(BillingRules.class);
        //编辑之前先校验原始规则是否被绑定
        BillingRules billingRulesOld = billingRulesMapper.selectById(billingRules.getId());
        if (!isChargeUuidUsed(billingRulesOld.getUuid()))//规则正在被使用，不允许修改
            throw new DataValidationException(ExceptionInfo.ALREADY_IN_USE);
        if (null == billingRules.getBillingCycleLimit())//周期额度为null，表示不限次数，值为-1
            billingRules.setBillingCycleLimit(-1L);
        isExist(billingRules);
        if (2 == billingRules.getBillingType()) {//按条计费，不要要限定时间和量
            billingRules.setBillingCycle(null);
            billingRules.setBillingCycleLimit(null);
        }
        billingRules.setName(getChargeUuidName(billingRules));
        billingRules.setUpdateTime(System.currentTimeMillis());
        billingRulesMapper.update(billingRules);
        redisService.set("chargeRule:" + billingRules.getUuid(), billingRules);
        return true;
    }

    /**
     * 分页查询所有可用规则
     */
    public Map<String, Object> show(PageInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        List<BillingRules> rulesList = billingRulesMapper.findByPage(pageInfo);
        Integer totalCount = billingRulesMapper.getTotalCount();
        pageInfo.setTotalCount(totalCount);
        map.put("list", rulesList);
        map.put("pageNum", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }

    /**
     * 根据规则uuid查询规则信息
     *
     * @param uuid
     * @return
     */
    @Override
    public BillingRules selectByBillingRules(String uuid) {
        JSONObject jsonObject = redisService.get("chargeRule:" + uuid);
        if (jsonObject != null) {
            BillingRules billingRules = jsonObject.toJavaObject(BillingRules.class);
            return billingRules;
        } else {
            log.debug("selectByBillingRules: {}", uuid);
            return billingRulesMapper.selectByBillingRulesUuid(uuid);
        }
    }

    @Override
    public List<BillingRules> chargeNameList() {
        List<BillingRules> rulesList = billingRulesMapper.query();
        return rulesList;
    }

    /**
     * 修改或者删除uuid时，需要先校验该条规则是否被绑定
     * 被绑定情况下不允许修改和删除规则
     *
     * @param chargeUuid
     * @return
     */
    public boolean isChargeUuidUsed(String chargeUuid) {
        //判断是否有数据源绑定规则uuid
        List<DatasourceCharge> datasourceChargeList = datasourceChargeService.getDatasourceChargeByBillingRulesUuid(chargeUuid);
        if (null != datasourceChargeList && datasourceChargeList.size() > 0)
            return false;
        //判断是否有appKey绑定规则uuid
        List<CompanyApps> companyAppsList = customerRechargeService.selectCompanyByStrategyUuid(chargeUuid);
        if (null != companyAppsList && companyAppsList.size() > 0)
            return false;
        //判断是否有合约绑定该规则uuid
        List<CompanyAppsAuth> authList = authService.selectAuthByStrategyUuid(chargeUuid);
        if (null != authList && authList.size() > 0)
            return false;
        return true;
    }
}
