package com.imjcker.manager.manage.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.AppCertification;
import com.imjcker.manager.manage.po.CurrentLimitStrategy;
import com.imjcker.manager.manage.vo.StrategyAuthQuery;
import com.lemon.common.vo.BaseQuery;

public interface StrategyAuthService {

	PageInfo<AppCertification> getStrategyList(BaseQuery query);

	Boolean strategyAuth(StrategyAuthQuery query);

	List<AppCertification> getAllAuthed(String strategyUuid);

	Boolean removeApp(Integer appId);

	List<CurrentLimitStrategy> getAllStrategy(String strategyName);

	/**
	 * 根据UUID查找限流策略并且状态为可用
	 * @param limitStrategyuuid
	 * @return
	 * @Version 2.0
	 * @author lpy
	 */
	CurrentLimitStrategy findStrategyByStrategyuuidAndStatusIsEnable(String limitStrategyuuid);

	/**
	 * @Title: 验证策略唯一性
	 * @return true-存在，false-不存在
	 */
	Boolean checkStrategyUnique(String name);
}
