package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiCombination;
import com.imjcker.manager.manage.po.ApiCombinationExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiCombinationMapper {
    long countByExample(ApiCombinationExample example);

    int deleteByExample(ApiCombinationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiCombination record);

    int insertSelective(ApiCombination record);

    List<ApiCombination> selectByExampleWithBLOBs(ApiCombinationExample example);

    List<ApiCombination> selectByExample(ApiCombinationExample example);

    ApiCombination selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiCombination record, @Param("example") ApiCombinationExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiCombination record, @Param("example") ApiCombinationExample example);

    int updateByExample(@Param("record") ApiCombination record, @Param("example") ApiCombinationExample example);

    int updateByPrimaryKeySelective(ApiCombination record);

    int updateByPrimaryKeyWithBLOBs(ApiCombination record);

    int updateByPrimaryKey(ApiCombination record);
}
