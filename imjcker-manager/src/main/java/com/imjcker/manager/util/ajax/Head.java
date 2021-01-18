package com.imjcker.common.ajax;

import java.io.Serializable;

/**
 * @author thh 2018-10-30
 * @version 1.0.0
 * description: 消息头；提供消息返回状态以及状态码和错误信息
 **/
public class Head implements Serializable {
    /**
     * 请求结果标识
     */
    private boolean flag;
    /**
     * 错误主代码
     */
    private int mainErrorNum;
    /**
     * 错误子代码
     */
    private int subErrorNum;
    /**
     * 错误消息
     */
    private String errmsg;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getMainErrorNum() {
        return mainErrorNum;
    }

    public void setMainErrorNum(int mainErrorNum) {
        this.mainErrorNum = mainErrorNum;
    }

    public int getSubErrorNum() {
        return subErrorNum;
    }

    public void setSubErrorNum(int subErrorNum) {
        this.subErrorNum = subErrorNum;
    }
}
