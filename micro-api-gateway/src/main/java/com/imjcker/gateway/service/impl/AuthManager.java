package com.imjcker.gateway.service.impl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.imjcker.gateway.util.AppkeyAuthEntity;
import com.imjcker.gateway.util.FeignClientManager;
import com.imjcker.gateway.util.IpAuthEntity;

@FeignClientManager
public interface AuthManager {

	@PostMapping(value = "/ipWhite/isExistOfIpWhite")
	String ipAuth(@RequestBody IpAuthEntity entity);

	@PostMapping(value = "/company/checkPermissions")
	String appKeyAuth(@RequestBody AppkeyAuthEntity entity);
}
