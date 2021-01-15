package com.imjcker.common.ajax;

import java.io.Serializable;

/**
 * @author thh 2018-10-30
 * @version 1.0.0
 * description: API返回结果对象，所有API都返回该对象，数据放于Body域中
 **/
public class ApiResult implements Serializable {
    /**
     * 消息头部
     */
    private Head head = new Head();

    /**
     * 消息体
     */
    private Body body = new Body();

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    /**
     * 获取一个错误的返回结果,不带错误代码
     *
     * @param errmsg 错误信息
     * @return ApiResult
     */
    public static ApiResult getErrmsgResult(String errmsg) {
        ApiResult result = new ApiResult();
        result.getHead().setFlag(false);
        result.getHead().setErrmsg(errmsg);
        return result;
    }

    /**
     * 获取错误结果
     *
     * @param errmsg       错误信息
     * @param mainErrorNum 主错误代码
     * @param subErrorNum  子错误代码
     * @return ApiResult
     */
    public static ApiResult getErrmsgResult(String errmsg, int mainErrorNum, int subErrorNum) {
        ApiResult result = new ApiResult();
        result.getHead().setFlag(false);
        result.getHead().setErrmsg(errmsg);
        result.getHead().setMainErrorNum(mainErrorNum);
        result.getHead().setSubErrorNum(subErrorNum);
        return result;
    }

    /**
     * 获取一个成功的请求结果
     *
     * @param data 数据域
     * @return ApiResult
     */
    public static ApiResult getSuccessResult(Object data) {
        ApiResult result = new ApiResult();
        result.getHead().setFlag(true);
        result.getBody().setData(data);
        return result;
    }
}
