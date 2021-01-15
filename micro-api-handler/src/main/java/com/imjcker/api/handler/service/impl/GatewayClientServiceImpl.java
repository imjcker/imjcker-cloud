package com.imjcker.api.handler.service.impl;

import com.imjcker.api.handler.config.GatewayClientServiceConfig;
import com.imjcker.api.handler.service.GatewayClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayClientServiceImpl implements GatewayClientService {
    @Autowired
    private GatewayClientServiceConfig gatewayClientServiceConfig;

    @Override
    public String getEnvironment() {
        return gatewayClientServiceConfig.getEnvironment();
    }


}
