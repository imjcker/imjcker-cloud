package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiCategory;
import com.imjcker.manager.manage.po.ApiCategoryExample;
import com.imjcker.manager.manage.po.ApiCategoryRelationExample;
import com.imjcker.manager.manage.vo.ApiVO;
import com.lemon.common.exception.vo.BusinessException;
import com.imjcker.manager.manage.mapper.ApiCategoryMapper;
import com.imjcker.manager.manage.mapper.ApiCategoryRelationMapper;
import com.imjcker.manager.manage.service.ApiCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ApiCategoryServiceImpl implements ApiCategoryService {
    private final ApiCategoryMapper apiCategoryMapper;
    private final ApiCategoryRelationMapper apiCategoryRelationMapper;

    public ApiCategoryServiceImpl(ApiCategoryMapper apiCategoryMapper, ApiCategoryRelationMapper apiCategoryRelationMapper) {
        this.apiCategoryMapper = apiCategoryMapper;
        this.apiCategoryRelationMapper = apiCategoryRelationMapper;
    }

    @Override
    public boolean insert(ApiCategory obj) {
        try {
            int i = apiCategoryMapper.insert(obj);
            return i == 1;
        } catch (DuplicateKeyException exception) {
            throw new BusinessException("领域分类已存在:" + obj.getName());
        }
    }

    @Override
    public boolean save(ApiCategory obj) {
        return this.insert(obj);
    }

    @Override
    public boolean update(ApiCategory obj) {
        try {
            int i = apiCategoryMapper.updateByPrimaryKeySelective(obj);
            return i == 1;
        } catch (DuplicateKeyException e) {
            throw new BusinessException("领域分类已存在");
        }
    }

    @Override
    public boolean delete(ApiCategory obj) {
        //校验api_category_relation是否存在关联的API,存在则不允许删除
        ApiCategoryRelationExample apiCategoryRelationExample = new ApiCategoryRelationExample();
        ApiCategoryRelationExample.Criteria criteria = apiCategoryRelationExample.createCriteria();
        criteria.andCategoryIdEqualTo(obj.getId());
        long count = apiCategoryRelationMapper.countByExample(apiCategoryRelationExample);
        if (count == 0) {
            return 1 == apiCategoryMapper.deleteByPrimaryKey(obj.getId());
        } else {
            log.error("领域[{}]下有关联的接口[{}]个,不能允许删除.", obj.getId(), count);
            throw new BusinessException("存在关联接口,不允许删除");
        }
    }

    @Override
    public ApiCategory findById(Integer id) {
        return apiCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ApiCategory> findAll() {
        ApiCategoryExample example = new ApiCategoryExample();
        return apiCategoryMapper.selectByExample(example);
    }

    @Override
    public PageInfo<ApiCategory> findForPage(Integer pageNum, Integer pageSize, ApiCategory o) {
        ApiCategoryExample example = new ApiCategoryExample();
        ApiCategoryExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(o.getName())) {
            criteria.andNameLike("%" + o.getName() + "%");
        }
        if (StringUtils.isNoneBlank(o.getDescription())) {
            criteria.andDescriptionLike("%" + o.getName() + "%");
        }

        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()-> apiCategoryMapper.selectByExample(example));
    }

    @Override
    public PageInfo<ApiCategory> findForPage(ApiCategory o) {
        ApiCategoryExample example = new ApiCategoryExample();
        ApiCategoryExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(o.getName())) {
            criteria.andNameLike("%" + o.getName() + "%");
        }
        if (StringUtils.isNoneBlank(o.getDescription())) {
            criteria.andDescriptionLike("%" + o.getName() + "%");
        }
        return PageHelper.startPage(o).doSelectPageInfo(()->apiCategoryMapper.selectByExample(example));
    }


    @Override
    public List<ApiVO> findApiByCategory(int id) {
        return apiCategoryRelationMapper.findApiByCategory(id);
    }
}
