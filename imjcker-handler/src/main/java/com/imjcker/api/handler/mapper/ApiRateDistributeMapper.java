package com.imjcker.api.handler.mapper;


import com.imjcker.api.handler.po.ApiRateDistribute;
import com.imjcker.api.handler.po.ApiRateDistributeExample;
import com.imjcker.api.handler.po.ApiRateDistributeWithBLOBs;
import com.imjcker.api.handler.po.ApiRateDistribute;
import com.imjcker.api.handler.po.ApiRateDistributeExample;
import com.imjcker.api.handler.po.ApiRateDistributeWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
}
