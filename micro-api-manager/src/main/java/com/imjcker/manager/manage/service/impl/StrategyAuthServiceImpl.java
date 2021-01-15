package com.imjcker.manager.manage.service.impl;

import java.util.List;

import com.imjcker.manager.manage.mapper.CurrentLimitStrategyMapper;
import com.imjcker.manager.manage.po.AppCertification;
import com.imjcker.manager.manage.po.AppCertificationExample;
import com.imjcker.manager.manage.po.CurrentLimitStrategy;
import com.imjcker.manager.manage.po.CurrentLimitStrategyExample;
import com.imjcker.manager.manage.vo.StrategyAuthQuery;
import com.lemon.common.exception.ExceptionInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.BaseQuery;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.AppCertificationMapper;
import com.imjcker.manager.manage.service.StrategyAuthService;

@Transactional
@Service
public class StrategyAuthServiceImpl implements StrategyAuthService {

	@Autowired
	private AppCertificationMapper appCertificationMapper;

	@Autowired
	private CurrentLimitStrategyMapper currentLimitStrategyMapper;

	@Override
	public PageInfo<AppCertification> getStrategyList(BaseQuery query) {
		AppCertificationExample example = new AppCertificationExample();
		AppCertificationExample.Criteria criteria = example.createCriteria();
		if (query.getOrder() != null) {
			criteria.andAppNameLike(StringUtils.wrap(query.getOrder().replace("_", "\\_"), "%"));
		}
		criteria.andStatusEqualTo(1);
		example.setOrderByClause("createTime DESC");
		//PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<AppCertification> list = appCertificationMapper.selectByExample(example);
		PageInfo<AppCertification> page = new PageInfo<AppCertification>(list);
		return page;
	}

	@Override
	public Boolean strategyAuth(StrategyAuthQuery query) {
		String uuid = query.getStrategyUuid();
		//授权
		String appIds = query.getAppIds();
		if (appIds == null) {
			return true;
		}
		String[] ids = appIds.split(",");
		AppCertification app = new AppCertification();
		for (String id : ids) {
			app.setId(Integer.parseInt(id));
			app.setLimitStrategyuuid(uuid);
			app.setUpdateTime(System.currentTimeMillis());
			appCertificationMapper.updateByPrimaryKeySelective(app);
		}
		return true;
	}

	@Override
	public List<AppCertification> getAllAuthed(String strategyUuid) {
		AppCertificationExample example = new AppCertificationExample();
		AppCertificationExample.Criteria criteria = example.createCriteria();
		criteria.andLimitStrategyuuidEqualTo(strategyUuid);
		return appCertificationMapper.selectByExample(example);
	}

	@Override
	public Boolean removeApp(Integer appId) {
		AppCertificationExample example = new AppCertificationExample();
		AppCertificationExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(appId);
		List<AppCertification> list = appCertificationMapper.selectByExample(example);
		AppCertification appCertification = list.get(0);
		appCertification.setLimitStrategyuuid(null);
		appCertificationMapper.updateByPrimaryKey(appCertification);
		return true;
	}

	@Override
	public List<CurrentLimitStrategy> getAllStrategy(String strategyName) {
		CurrentLimitStrategyExample example = new CurrentLimitStrategyExample();
		CurrentLimitStrategyExample.Criteria criteria = example.createCriteria();
		if (strategyName != null) {
			criteria.andNameLike(StringUtils.wrap(strategyName, "%"));
		}
		criteria.andStatusEqualTo(1);
		example.setOrderByClause("createTime DESC");
		return currentLimitStrategyMapper.selectByExample(example);
	}

	//接口2.0 BY.lpy
	@Override
	public CurrentLimitStrategy findStrategyByStrategyuuidAndStatusIsEnable(String limitStrategyuuid) {
		if (StringUtils.isBlank(limitStrategyuuid)) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_LIMIT_STRATEGY_UUID);
		}
		CurrentLimitStrategyExample example = new CurrentLimitStrategyExample();
		CurrentLimitStrategyExample.Criteria criteria = example.createCriteria();
		criteria.andUuidEqualTo(limitStrategyuuid);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<CurrentLimitStrategy> list = currentLimitStrategyMapper.selectByExample(example);
		if (null == list || list.size() == 0) {
			throw new BusinessException(ExceptionInfo.NOT_EXIST_LIMIT_STRATEGY);
		}
		return list.get(0);
	}

	@Override
	public Boolean checkStrategyUnique(String name) {
		CurrentLimitStrategyExample example = new CurrentLimitStrategyExample();
		CurrentLimitStrategyExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		int count = currentLimitStrategyMapper.countByExample(example);
		return count != 0;
	}


}
