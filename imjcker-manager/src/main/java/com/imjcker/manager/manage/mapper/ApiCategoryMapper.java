package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiCategory;
import com.imjcker.manager.manage.po.ApiCategoryExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiCategoryMapper {
    long countByExample(ApiCategoryExample example);

    int deleteByExample(ApiCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiCategory record);

    int insertSelective(ApiCategory record);

    List<ApiCategory> selectByExample(ApiCategoryExample example);

    ApiCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiCategory record, @Param("example") ApiCategoryExample example);

    int updateByExample(@Param("record") ApiCategory record, @Param("example") ApiCategoryExample example);

    int updateByPrimaryKeySelective(ApiCategory record);

    int updateByPrimaryKey(ApiCategory record);
}
