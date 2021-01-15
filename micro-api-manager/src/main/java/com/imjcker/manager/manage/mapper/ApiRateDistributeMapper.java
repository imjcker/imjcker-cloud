package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiRateDistribute;
import com.imjcker.manager.manage.po.ApiRateDistributeExample;
import com.imjcker.manager.manage.po.ApiRateDistributeWithBLOBs;
import com.imjcker.manager.manage.po.ApiRateDistributeWithDictionary;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiRateDistributeMapper {
    int countByExample(ApiRateDistributeExample example);

    int deleteByExample(ApiRateDistributeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiRateDistributeWithBLOBs record);

    int insertSelective(ApiRateDistributeWithBLOBs record);

    List<ApiRateDistributeWithBLOBs> selectByExampleWithBLOBs(ApiRateDistributeExample example);

    List<ApiRateDistribute> selectByExample(ApiRateDistributeExample example);

    ApiRateDistributeWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiRateDistributeWithBLOBs record, @Param("example") ApiRateDistributeExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiRateDistributeWithBLOBs record, @Param("example") ApiRateDistributeExample example);

    int updateByExample(@Param("record") ApiRateDistribute record, @Param("example") ApiRateDistributeExample example);

    int updateByPrimaryKeySelective(ApiRateDistributeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ApiRateDistributeWithBLOBs record);

    int updateByPrimaryKey(ApiRateDistribute record);
    List<ApiRateDistributeWithDictionary> selectByExampleWithDictionary(ApiRateDistributeExample example);
}
