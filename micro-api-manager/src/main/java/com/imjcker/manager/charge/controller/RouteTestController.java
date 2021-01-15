package com.imjcker.manager.charge.controller;

import com.lemon.common.vo.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ztzh_tanhh 2020/2/24
 **/
@RestController
public class RouteTestController {
    @GetMapping("/test")
    public CommonResult test() {
        return CommonResult.success();
    }
}
