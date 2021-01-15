package com.imjcker.manager.charge.service;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.CompanyAppParam;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.vo.request.ReqAppkeyByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.imjcker.manager.charge.po.CompanyAppParam;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsRecharge;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.vo.request.ReqAppkeyByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;

import java.util.List;

/**
 * @Author WT
 * @Date 10:35 2020/2/24
 * @Version CustomerRechargeService v1.0
 * @Desicrption
 */
public interface CustomerRechargeService {
    PageInfo<CompanyAppsVo> queryCustomer(CompanyAppParam appParam);

    void charge(CompanyAppsRecharge appRecharge);

    PageInfo<CompanyAppsRecharge> queryChargeRecord(CompanyAppParam appParam);

    void addCustomer(CompanyApps app);

    void updateCustomer(CompanyApps app);

    void deleteCustomer(CompanyApps companyApps);

    List<CompanyApps> selectCompanyByStrategyUuid(String strategyUuid);

    /**
     * @Description : 客户余额余量记录 分页查询
     * @Date : 2020/4/1 10:12
     * @Auth : qiuwen
     */
    PageInfo<RespCompanyBalanceHistory> queryBalancePage(ReqAppkeyByPage req);

}
