package com.imjcker.api.handler.mapper;

import com.imjcker.api.handler.model.BranchBankSourceAccount;
import com.imjcker.api.handler.model.BranchBankSourceAccount;
import com.imjcker.api.handler.po.ApiInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WT
 * @Date 17:29 2019/8/20
 * @Version ApiInfoMapper v1.0
 * @Desicrption
 */
@Mapper
public interface ApiInfoMapper {
    ApiInfoResponse findApiInfoById(@Param("apiId") Integer apiId);

    List<BranchBankSourceAccount> getBranchBankAccountList(@Param("apiGroupId") Integer apiGroupId,
                                                           @Param("appKey") String appKey);
}
