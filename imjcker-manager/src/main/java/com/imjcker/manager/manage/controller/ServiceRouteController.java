package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.ServiceRouter;
import com.imjcker.manager.manage.service.ServiceRouteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceRoute")
public class ServiceRouteController {
    private final ServiceRouteService serviceRouteService;

    @Autowired
    public ServiceRouteController(ServiceRouteService serviceRouteService) {
        this.serviceRouteService = serviceRouteService;
    }

    @PostMapping("/index")
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        ServiceRouter.VO serviceRouterVO = jsonObject.toJavaObject(ServiceRouter.VO.class);
        PageInfo<ServiceRouter> page = serviceRouteService.findForPage(serviceRouterVO.getPageNum(), serviceRouterVO.getPageSize(), serviceRouterVO);
        return CommonResult.success(page);
    }


    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public CommonResult add(@RequestBody JSONObject jsonObject) {
        ServiceRouter serviceRouter = jsonObject.toJavaObject(ServiceRouter.class);
        if (StringUtils.isBlank(serviceRouter.getPath()) || StringUtils.isBlank(serviceRouter.getUrl()) || StringUtils.isBlank(serviceRouter.getName()))
            return CommonResult.fail(ResultStatusEnum.PARAM_ERROR);
        ResultStatusEnum status = serviceRouteService.checkUnique(serviceRouter);
        if (!status.equals(ResultStatusEnum.SUCCESS))
            return CommonResult.fail(status);
        serviceRouteService.insert(serviceRouter);
        return CommonResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        ServiceRouter serviceRouter = jsonObject.toJavaObject(ServiceRouter.class);
        serviceRouteService.delete(serviceRouter);
        return CommonResult.success();
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateStrategy(@RequestBody JSONObject jsonObject) {
        ServiceRouter serviceRouter = jsonObject.toJavaObject(ServiceRouter.class);
        if (StringUtils.isBlank(serviceRouter.getPath()) || StringUtils.isBlank(serviceRouter.getUrl()) || StringUtils.isBlank(serviceRouter.getName()))
            return CommonResult.fail(ResultStatusEnum.PARAM_ERROR);
        ResultStatusEnum status = serviceRouteService.checkUnique(serviceRouter);
        if (!status.equals(ResultStatusEnum.SUCCESS))
            return CommonResult.fail(status);
        serviceRouteService.update(serviceRouter);
        return CommonResult.success();
    }
}
