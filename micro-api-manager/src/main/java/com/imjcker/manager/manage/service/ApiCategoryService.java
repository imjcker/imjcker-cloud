package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.po.ApiCategory;
import com.imjcker.manager.manage.vo.ApiVO;

import java.util.List;

public interface ApiCategoryService extends BaseService<ApiCategory> {


    /**
     * 查询指定分组下的接口列表
     *
     * @param id 查询条件对象
     * @return id 所属分类下的所有接口
     */
    List<ApiVO> findApiByCategory(int id);
}
