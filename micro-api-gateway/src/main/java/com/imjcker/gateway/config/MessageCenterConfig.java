package com.imjcker.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "messageCenter")
@Component
public class MessageCenterConfig {

    private String url;

    private String reqChannel;

    private String templetNo;

    private String msgTheme;

    private String mobile;

    private String empNo;

    private String toChannel;

    private String phoneChannel;

    private int wxMinute;

    private int wxHour;

    private int wxDay;

    private int  phoneMinute;

    private int phoneHour;

    private int phoneDay;

    public String getPhoneChannel() {
        return phoneChannel;
    }

    public void setPhoneChannel(String phoneChannel) {
        this.phoneChannel = phoneChannel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public String getMsgTheme() {
        return msgTheme;
    }

    public void setMsgTheme(String msgTheme) {
        this.msgTheme = msgTheme;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getWxMinute() {
        return wxMinute;
    }

    public void setWxMinute(int wxMinute) {
        this.wxMinute = wxMinute;
    }

    public int getWxHour() {
        return wxHour;
    }

    public void setWxHour(int wxHour) {
        this.wxHour = wxHour;
    }

    public int getWxDay() {
        return wxDay;
    }

    public void setWxDay(int wxDay) {
        this.wxDay = wxDay;
    }

    public int getPhoneMinute() {
        return phoneMinute;
    }

    public void setPhoneMinute(int phoneMinute) {
        this.phoneMinute = phoneMinute;
    }

    public int getPhoneHour() {
        return phoneHour;
    }

    public void setPhoneHour(int phoneHour) {
        this.phoneHour = phoneHour;
    }

    public int getPhoneDay() {
        return phoneDay;
    }

    public void setPhoneDay(int phoneDay) {
        this.phoneDay = phoneDay;
    }
}
