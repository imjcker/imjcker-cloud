package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.po.ServiceRouter;
import com.lemon.common.vo.ResultStatusEnum;

public interface ServiceRouteService extends BaseService<ServiceRouter> {

    /**
     * check unique
     *
     * @param serviceRouter target object
     * @return check result
     */
    ResultStatusEnum checkUnique(ServiceRouter serviceRouter);
}
