package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.mapper.ServiceRouterMapper;
import com.imjcker.manager.manage.po.ServiceRouter;
import com.imjcker.manager.manage.po.ServiceRouterExample;
import com.lemon.common.util.RedisUtil;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.ServiceRouteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRouteServiceImpl implements ServiceRouteService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ServiceRouterMapper serviceRouterMapper;

    @Autowired
    public ServiceRouteServiceImpl(ServiceRouterMapper serviceRouterMapper) {
        this.serviceRouterMapper = serviceRouterMapper;
    }

    @Override
    public boolean insert(ServiceRouter obj) {
        int insert = serviceRouterMapper.insert(obj);
        if (insert == 1) {
            RedisUtil.delete("serviceRouters");
        }
        return insert == 1;
    }

    @Override
    public boolean save(ServiceRouter obj) {
        int i = serviceRouterMapper.insertSelective(obj);
        if (i == 1)
            RedisUtil.delete("serviceRouters");
        return i == 1;
    }

    @Override
    public boolean update(ServiceRouter router) {
        int i = serviceRouterMapper.updateByPrimaryKeySelective(router);
        if (i == 1)
            RedisUtil.delete("serviceRouters");
        return i == 1;
    }

    @Override
    public boolean delete(ServiceRouter router) {
        int i = serviceRouterMapper.deleteByPrimaryKey(router.getId());
        if (i == 1)
            RedisUtil.delete("serviceRouters");
        return i ==1;
    }

    @Override
    public ServiceRouter findById(Integer id) {
        return serviceRouterMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<ServiceRouter> findForPage(Integer pageNum, Integer pageSize, ServiceRouter router) {
        ServiceRouterExample example = new ServiceRouterExample();
        ServiceRouterExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(router.getName())) {
            criteria.andNameLike("%" + router.getName().trim() + "%");
        }
        if (StringUtils.isNotEmpty(router.getPath())) {
            criteria.andPathLike("%" + router.getPath().trim() + "%");
        }
        if (StringUtils.isNotEmpty(router.getServiceId())) {
            criteria.andServiceIdLike("%" + router.getServiceId().trim() + "%");
        }
        if (StringUtils.isNotEmpty(router.getUrl())) {
            criteria.andUrlLike("%" + router.getUrl().trim() + "%");
        }
        example.setOrderByClause("path desc");
        PageHelper.startPage(pageNum, pageSize);
        List<ServiceRouter> serviceRouters = serviceRouterMapper.selectByExample(example);
        return new PageInfo<>(serviceRouters);
    }

    @Override
    public PageInfo<ServiceRouter> findForPage(ServiceRouter obj) {
        return null;
    }

    @Override
    public List<ServiceRouter> findAll() {
        ServiceRouterExample example = new ServiceRouterExample();
        example.createCriteria().andEnabledNotEqualTo(1);
        example.setOrderByClause("path desc");
        return serviceRouterMapper.selectByExample(example);
    }

    @Override
    public ResultStatusEnum checkUnique(ServiceRouter router) {
        ServiceRouterExample example = new ServiceRouterExample();
        //id 为空则是新增检查,不为空则是更新检查
        if (StringUtils.isNotEmpty(router.getName())) {
            ServiceRouterExample.Criteria criteria1 = example.createCriteria();
            if (router.getId() != null)
                criteria1.andIdNotEqualTo(router.getId());
            criteria1.andNameEqualTo(router.getName());
            if (serviceRouterMapper.countByExample(example) > 0) {
                log.error("check name failed");
                return ResultStatusEnum.UNIQUE_ERROR_NAME;
            }
        }
        if (StringUtils.isNotEmpty(router.getPath())) {
            ServiceRouterExample.Criteria criteria2 = example.createCriteria();
            if (router.getId() != null)
                criteria2.andIdNotEqualTo(router.getId());
            criteria2.andPathEqualTo(router.getPath());
            example.or(criteria2);
            if (serviceRouterMapper.countByExample(example) > 0) {
                log.error("check path failed");
                return ResultStatusEnum.UNIQUE_ERROR_PATH;
            }
        }
        return ResultStatusEnum.SUCCESS;
    }
}
