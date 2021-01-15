package com.imjcker.manager.manage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiInfoWithSubApi;
import com.imjcker.manager.manage.po.ApiRateDistribute;
import com.imjcker.manager.manage.po.ApiRateDistributeWithBLOBs;
import com.imjcker.manager.manage.po.BackendDistributeParams;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.SubApiWeightQuery;
import com.imjcker.manager.manage.po.*;

/**
 * @author Lemon.kiana
 * @version 2.0
 *          2017年9月20日 上午10:22:27
 * @Title SubUpStreamApiService
 * @Description 子上游API服务接口层
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
public interface SubUpStreamApiService {

    Map<String, Object> get(ApiRateDistributeWithBLOBs apiRateDistribute);

    void updateApiName(ApiRateDistribute apiRateDistribute);

    int save(ApiRateDistributeWithBLOBs subThirdApiInfo,
             List<BackendDistributeParams> subThirdRequestInfo);

    void del(ApiRateDistribute apiRateDistribute);

    List<SubApiWeightQuery> getWeightConfigs(ApiRateDistribute apiRateDistribute);

    List<SubApiWeightQuery> updateWeightConfigs(List<SubApiWeightQuery> list);

    PageInfo<ApiInfoWithSubApi> query(ApiInfoWithSubApiQuery query);

    List<ApiRateDistributeWithBLOBs> querySubApiListByApiId(Integer apiId);

	/**
	 * 保存上游参数
	 * @param bDParam
	 * @Version 2.0
	 */
	void saveBackendParams(BackendDistributeParams bDParam);
}
