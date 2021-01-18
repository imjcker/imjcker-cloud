package com.imjcker.api.handler.mapper;

import com.imjcker.api.handler.model.ApiCombinationInfo;
import com.imjcker.api.handler.model.ApiCombinationInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WT
 * @Date 15:17 2019/8/20
 * @Version CombInfMapper v1.0
 * @Desicrption
 */
@Mapper
public interface CombInfMapper {
    List<ApiCombinationInfo> findApiWithCombId(@Param("combinationId") String cAppId);
}
