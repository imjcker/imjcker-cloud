package com.imjcker.manager.manage.service;

/**
 * @Author WT
 * @Date 9:00 2019/8/23
 * @Version ModifyCombinationParamService v1.0
 * @Desicrption
 */
public interface ModifyCombinationParamService {
    void updateCombInfWithPublish(Integer apiId);

    void updateCombInfWithStatus(Integer apiId, Integer status);

    void updateCombinationParams(Integer apiId);
}
