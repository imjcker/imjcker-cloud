package com.imjcker.gateway.util;

import com.netflix.zuul.http.ServletInputStreamWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @Author WT
 * @Date 17:35 2020/4/24
 * @Version RequestBuildUtil v1.0
 * @Desicrption
 */
public class RequestBuildUtil {

    public static HttpServletRequest build(String body,HttpServletRequest request) {
        final byte[] bodyBytes = body.getBytes();
        return new HttpServletRequestWrapper(request) {
            @Override
            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStreamWrapper(bodyBytes);
            }

            @Override
            public int getContentLength() {
                return bodyBytes.length;
            }

            @Override
            public long getContentLengthLong() {
                return bodyBytes.length;
            }
        };
    }
}
