package com.imjcker.manager.scheduled.controller;

import com.imjcker.manager.scheduled.service.NetPingService;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author WT
 * @Date 10:39 2019/8/2
 * @Version ${version}
 */
@RestController
public class CheckSourceNetController {

    @Autowired
    private NetPingService netPingService;

    @RequestMapping("/checkSourceNet")
    public CommonResult startCheck() {
        netPingService.startPing();
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }
}
