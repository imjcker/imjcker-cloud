package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.WhiteIpListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/ipWhite")
public class WhiteIpListController {

    @Autowired
    private WhiteIpListService whiteIpListService;

    @PostMapping("/show")
    public CommonResult show(@RequestBody JSONObject jsonObject){
        Map<String, Object> result = whiteIpListService.show(jsonObject);
        return new CommonResult(ResultStatusEnum.SUCCESS,result);
    }

    @PostMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject){
        if(!whiteIpListService.delete(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR,null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }

    @PostMapping("/search")
    public CommonResult search(@RequestBody JSONObject jsonObject){
        Map<String, Object> result = whiteIpListService.search(jsonObject);
        return new CommonResult(ResultStatusEnum.SUCCESS,result);
    }

    @PostMapping("/save")
    public CommonResult save(@RequestBody JSONObject jsonObject) throws ParseException {
        if (!whiteIpListService.save(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR,null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }

    @PostMapping("/edit")
    public CommonResult edit(@RequestBody JSONObject jsonObject) throws ParseException {
        if (!whiteIpListService.edit(jsonObject)) {
            return new CommonResult(ResultStatusEnum.ERROR,null);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }
    @GetMapping("/syncRedis")
    public CommonResult syncRedis() {
        whiteIpListService.syncRedis();
        return new CommonResult(ResultStatusEnum.SUCCESS,null);
    }
}
