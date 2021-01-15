package com.imjcker.manager.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author yezhiyuan
 * @version V1.0
 */
public class XssFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {

            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = cleanXSS(values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }
            return cleanXSS(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null)
                return null;

            return cleanXSS(value);
        }

        /**
         * 替换xss攻击内容
         */
        private String cleanXSS(String value) {
            if (value != null) {
                value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
                value = value.replaceAll("\\(", "（").replaceAll("\\)",
                        "）");
                value = value.replaceAll("'", "& #39;");
                value = value.replaceAll("eval\\((.*)\\)", "");
                value = value.replaceAll(
                        "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
                value = value.replaceAll("script", "");
            }
            return value;
        }

    }
}
