package com.imjcker.gateway.mapper;

import java.util.List;

import com.imjcker.gateway.po.CurrentLimitStrategy;
import com.imjcker.gateway.po.CurrentLimitStrategyExample;
import org.apache.ibatis.annotations.Param;

public interface CurrentLimitStrategyMapper {
    int countByExample(CurrentLimitStrategyExample example);

    int deleteByExample(CurrentLimitStrategyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CurrentLimitStrategy record);

    int insertSelective(CurrentLimitStrategy record);

    List<CurrentLimitStrategy> selectByExample(CurrentLimitStrategyExample example);

    CurrentLimitStrategy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CurrentLimitStrategy record, @Param("example") CurrentLimitStrategyExample example);

    int updateByExample(@Param("record") CurrentLimitStrategy record, @Param("example") CurrentLimitStrategyExample example);

    int updateByPrimaryKeySelective(CurrentLimitStrategy record);

    int updateByPrimaryKey(CurrentLimitStrategy record);
}
