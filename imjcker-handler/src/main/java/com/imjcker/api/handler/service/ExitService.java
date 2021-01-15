package com.imjcker.api.handler.service;


import com.imjcker.api.common.vo.Result;

import java.io.IOException;
import java.util.Map;

public interface ExitService {
    Result commonOkHttpRequest(String method, String url, Map<String, String> bodyVariables,
                               Map<String, String> queryVariables, Map<String, String> headerVariables,
                               String json, String xml, Long timeout, String protocol,
                               String retryFlag, int retryCount, boolean isIgnoreVerify);

    Result commonHttpClientRequest(String url, String method, Map<String, String> header, String params, Map<String, String> mapParams, String encoding, long timeout, boolean isIgnoreVerify) throws IOException;


    Result socketRequest(String method, String url, Map<String, String> bodyVariables, Map<String, String> queryVariables, Map<String, String> headerVariables, String json, String xml, Long timeout, String protocol, String retryFlag, int retryCount, boolean isIgnoreVerify);
}
