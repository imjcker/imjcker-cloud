package com.imjcker.gateway.service;

import com.alibaba.fastjson.JSONObject;

public interface MessageCenterService {

    String sendMessage(JSONObject content,boolean sendToPhone);

    int getWxIntervalTime(int timeFlag);

    int getPhoneIntervalTime(int timeFlag);
}
