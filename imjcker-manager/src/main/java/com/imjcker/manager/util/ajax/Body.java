package com.imjcker.common.ajax;

import java.io.Serializable;

/**
 * @author thh 2018-10-30
 * @version 1.0.0
 * description: 消息体
 **/
public class Body implements Serializable {
    /**数据域*/
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
