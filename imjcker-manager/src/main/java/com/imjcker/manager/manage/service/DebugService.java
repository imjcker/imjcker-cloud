package com.imjcker.manager.manage.service;

import java.util.Map;

import com.imjcker.manager.manage.vo.DebugApiQuery;

public interface DebugService {

	Map<String, Object> getApiDebugInfo(DebugApiQuery query);
}
