package com.imjcker.manager.manage.controller;

import com.imjcker.manager.manage.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户接口权限管理类
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 列表页面
     *
     * @param jsonObject
     * @return
     */
//    @PostMapping("/search")
//    public CommonResult search(@RequestBody JSONObject jsonObject) {
//        if (jsonObject.isEmpty())
//            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
//        Map<String, Object> result = authService.search(jsonObject);
//        return new CommonResult(ResultStatusEnum.SUCCESS, result);
//    }
//
//    /**
//     * 删除(客户appKey删除，对应的合约也应该删除)
//     *
//     * @param jsonObject
//     * @return
//     */
//    @PostMapping("/delete")
//    public CommonResult delete(@RequestBody JSONObject jsonObject) {
//        if (jsonObject.isEmpty() || null == jsonObject.getInteger("id"))
//            return new CommonResult(ResultStatusEnum.PARAMS_INPUT_NULL, null);
//        if (!authService.delete(jsonObject.getInteger("id"))) {
//            return new CommonResult(ResultStatusEnum.ERROR, null);
//        }
//        return new CommonResult(ResultStatusEnum.SUCCESS, null);
//    }
//
//    /**
//     * 新增
//     *
//     * @param jsonObject
//     * @return
//     * @throws ParseException
//     */
//    @PostMapping("/save")
//    public CommonResult save(@RequestBody JSONObject jsonObject) {
//
//        if (!authService.save(jsonObject)) {
//            return new CommonResult(ResultStatusEnum.ERROR, null);
//        }
//        return new CommonResult(ResultStatusEnum.SUCCESS, null);
//    }
//
//    /**
//     * 编辑(只允许编辑客户描述和客户名称，appKey不允许修改，因为修改appKey后，已经建立的合约就会存在合约但是客户不存在的情况)
//     *
//     * @param jsonObject
//     * @return
//     * @throws ParseException
//     */
//    @PostMapping("/edit")
//    public CommonResult edit(@RequestBody JSONObject jsonObject) throws ParseException {
//        if (!authService.edit(jsonObject)) {
//            return new CommonResult(ResultStatusEnum.ERROR, null);
//        }
//        return new CommonResult(ResultStatusEnum.SUCCESS, null);
//    }
}
