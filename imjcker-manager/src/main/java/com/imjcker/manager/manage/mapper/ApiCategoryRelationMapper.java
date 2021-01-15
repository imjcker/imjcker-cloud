package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiCategoryRelation;
import com.imjcker.manager.manage.po.ApiCategoryRelationExample;
import com.imjcker.manager.manage.vo.ApiVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ApiCategoryRelationMapper {
    long countByExample(ApiCategoryRelationExample example);

    int deleteByExample(ApiCategoryRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiCategoryRelation record);

    int insertSelective(ApiCategoryRelation record);

    List<ApiCategoryRelation> selectByExample(ApiCategoryRelationExample example);

    ApiCategoryRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiCategoryRelation record, @Param("example") ApiCategoryRelationExample example);

    int updateByExample(@Param("record") ApiCategoryRelation record, @Param("example") ApiCategoryRelationExample example);

    int updateByPrimaryKeySelective(ApiCategoryRelation record);

    int updateByPrimaryKey(ApiCategoryRelation record);

    List<ApiVO> findApiByCategory(Integer categoryId);
}
