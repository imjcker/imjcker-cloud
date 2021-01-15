package com.imjcker.manager.charge.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.request.ReqCheckUpdateDatasource;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.vo.request.ReqCheckUpdateDatasource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ztzh_tanhh 2020/3/17
 * 上游接口计费管理service
 **/
public interface DatasourceChargeService {
    /**
     * 分页查询
     *
     * @param jsonObject 条件
     * @return 分页结果
     */
    PageInfo<DatasourceCharge> selectForPage(JSONObject jsonObject);

    /**
     * 新增
     *
     * @param jsonObject 对象
     * @return 结果
     */
    boolean save(JSONObject jsonObject);

    /**
     * 更新
     *
     * @param id 接口
     * @return 结果
     */
    boolean update(Integer id);

    /**
     * @Description : 编辑上游计费 核算是否亏损
     * @Date : 2020/5/9 17:26
     * @Auth : qiuwen
     */
    Map<String, BigDecimal> checkUpdateInfo(ReqCheckUpdateDatasource req);

    /**
     * 删除
     *
     * @param jsonObject 对象
     * @return 结果
     */
    boolean delete(JSONObject jsonObject);

    /**
     * 通过apiId获取
     *
     * @param jsonObject 对象
     * @return 结果
     */
    DatasourceCharge getChargeByApiId(JSONObject jsonObject) ;

    DatasourceCharge getChargeByApiId(Integer apiId);

    /**
     * 通过 BillingRulesUuid 获取
     *
     * @param chargeUuid
     * @return 结果
     */
    List<DatasourceCharge> getDatasourceChargeByBillingRulesUuid(String chargeUuid) ;
}
