package com.imjcker.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.domain.User;
import com.imjcker.domain.JsonResult;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private OauthConfigProperties oauthConfigProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("token");
        if (user != null) {
            if (user.getLogin().equals(oauthConfigProperties.getUser())) {
                return true;
            } else {
                log.error("登录了非指定用户：{}", user.getLogin());
                setResponse(response, "没有授权的用户：" + user.getLogin());
                return false;
            }
        }
        setResponse(response, "没有登录");
        return false;
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        JsonResult jsonResult = JsonResult.fail(message);
        JSONObject json = (JSONObject) JSON.toJSON(jsonResult);
        writer.write(json.toString());
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
