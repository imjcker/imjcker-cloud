package com.imjcker.api.handler.mapper;

import com.imjcker.api.handler.po.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author WT
 * @Date 16:51 2019/8/20
 * @Version ParamsMapper v1.0
 * @Desicrption
 */
@Mapper
public interface ParamsMapper {
    List<ApiInfoVersionsWithBLOBs> selectByExampleWithBLOBs(ApiInfoVersionsExample example);

    List<BackendRequestParamsVersions> selectBackExample(BackendRequestParamsVersionsExample example);

    List<RequestParamsVersions> selectRequestExample(RequestParamsVersionsExample example);
}
