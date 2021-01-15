package com.imjcker.manager.charge.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.request.ReqStockByPage;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;

import java.util.List;

public interface AuthService {


    boolean delete(Integer id);

    PageInfo<CompanyAppsAuthVo> search(JSONObject jsonObject);

    boolean save(JSONObject jsonObject);

    boolean edit(JSONObject jsonObject) ;

    void deleteByApiId(Integer apiId);

    List<CompanyAppsAuth> selectAuthByStrategyUuid(String strategyUuid);

    void checkPrice(CompanyAppsAuth companyAppsAuth, DatasourceCharge datasourceCharge);

    void checkChargeMode(CompanyAppsAuth companyAppsAuth);

    /**
     * @Description : 接口调用记录 分页
     * @Date : 2020/4/1 10:29
     * @Auth : qiuwen
     */
    PageInfo<RespAuthStockHistory> queryStockPage(ReqStockByPage req);

    Boolean checkApiAuth(Integer apiId);

    /**
     * 查询客户公钥 私钥
     * @param appKey
     * @return
     */
    RespCompanyKey selectKey(String appKey);

    /**
     * 运维接口,批量更新数据库的密钥
     */
    void updateForKey();
}
