package com.imjcker.api.handler.model;

import java.io.Serializable;

/**
 * @author thh  2019/7/24
 * @version 1.0.0
 **/
public class AsyncCollectModel implements Serializable {
    private long startTime;

    private AsyncModel asyncModel;

    private InterfaceCountModel countModel;

    public AsyncCollectModel() {
    }

    public AsyncCollectModel(long startTime, AsyncModel asyncModel, InterfaceCountModel countModel) {
        this.startTime = startTime;
        this.asyncModel = asyncModel;
        this.countModel = countModel;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public AsyncModel getAsyncModel() {
        return asyncModel;
    }

    public void setAsyncModel(AsyncModel asyncModel) {
        this.asyncModel = asyncModel;
    }

    public InterfaceCountModel getCountModel() {
        return countModel;
    }

    public void setCountModel(InterfaceCountModel countModel) {
        this.countModel = countModel;
    }
}
