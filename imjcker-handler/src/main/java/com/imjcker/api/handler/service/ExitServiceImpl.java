package com.imjcker.api.handler.service;

import com.imjcker.api.common.http.proxy.SocketUtil;
import com.imjcker.api.common.vo.Result;
import com.imjcker.api.handler.util.ConstantEnum;
import com.imjcker.api.handler.util.HttpClientUtil;
import com.imjcker.api.handler.util.RequestUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ExitServiceImpl implements ExitService {
    private static final String ENCODING = "UTF-8";

    /**
     * OKHttp通用
     */
    @Override
    public Result commonOkHttpRequest(String method, String url, Map<String, String> bodyVariables, Map<String, String> queryVariables,
                                      Map<String, String> headerVariables, String json, String xml, Long timeout, String protocol,
                                      String retryFlag, int retryCount, boolean isIgnoreVerify) {
        return RequestUtil.sendRequest1(method, url, bodyVariables, queryVariables, headerVariables, json, xml, timeout, protocol, retryFlag, retryCount, isIgnoreVerify);
    }

    /**
     * HttpClient通用
     */
    @Override
    public Result commonHttpClientRequest(String url, String method, Map<String, String> header, String params, Map<String, String> mapParams, String encoding, long timeout, boolean isIgnoreVerify) throws IOException {
        Result result;
        if (ConstantEnum.HttpMethod.post.getName().equals(method))
            result = HttpClientUtil.doPost(url, header, params, ENCODING, timeout, isIgnoreVerify);
        else {
            result = HttpClientUtil.doGet(url, header, params, ENCODING, timeout);
        }
        return result;
    }


    @Override
    public Result socketRequest(String method, String url, Map<String, String> bodyVariables, Map<String, String> queryVariables, Map<String, String> headerVariables, String json, String xml, Long timeout, String protocol, String retryFlag, int retryCount, boolean isIgnoreVerify) {
        String[] hostAndPort = url.split(":");
        return SocketUtil.send2TcpServer(xml, hostAndPort[0], Integer.parseInt(hostAndPort[1]), timeout.intValue());
    }

}
