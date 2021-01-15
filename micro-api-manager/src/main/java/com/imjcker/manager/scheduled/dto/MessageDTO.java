package com.imjcker.manager.scheduled.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {

    private String reqChannel;

    private String templetNo;

    private String msgTheme;

    private String mobile;

    private String cifNo;

    private String empNo;

    private String toChannel;

    private String url;

    private long reqJnlno;

    private String reqTime;

    private String reqDate;

    private String msgContent;

    public String getReqChannel() {
        return reqChannel;
    }

    public void setReqChannel(String reqChannel) {
        this.reqChannel = reqChannel;
    }

    public String getTempletNo() {
        return templetNo;
    }

    public void setTempletNo(String templetNo) {
        this.templetNo = templetNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCifNo() {
        return cifNo;
    }

    public void setCifNo(String cifNo) {
        this.cifNo = cifNo;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getToChannel() {
        return toChannel;
    }

    public void setToChannel(String toChannel) {
        this.toChannel = toChannel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getReqJnlno() {
        return reqJnlno;
    }

    public void setReqJnlno(long reqJnlno) {
        this.reqJnlno = reqJnlno;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTheme() {
        return msgTheme;
    }

    public void setMsgTheme(String msgTheme) {
        this.msgTheme = msgTheme;
    }

}
