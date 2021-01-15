package com.imjcker.api;

import com.imjcker.config.OauthConfigProperties;
import com.imjcker.config.SiteConfig;
import com.imjcker.domain.User;
import com.imjcker.domain.JsonResult;
import com.imjcker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class GlobalController {
    private final UserService userService;
    private final OauthConfigProperties oauthConfigProperties;
    private final SiteConfig siteConfig;

    @Autowired
    public GlobalController(UserService userService, OauthConfigProperties oauthConfigProperties, SiteConfig siteConfig) {
        this.userService = userService;
        this.oauthConfigProperties = oauthConfigProperties;
        this.siteConfig = siteConfig;
    }

    @GetMapping("/oauth/github/url")
    public JsonResult getUrl(String state) {
        System.out.println("state: " + state);
        String url = oauthConfigProperties.getAuthorize() +
                "?client_id=" + oauthConfigProperties.getClientId() +
                "&redirect_uri=" + oauthConfigProperties.getRedirect_uri() +
                "&state=" + state;
        log.info("登录地址：" + url);

        return JsonResult.success(url);
    }

    @GetMapping("/oauth/github/callback")
    public void loginGithub(HttpServletResponse response, HttpServletRequest request, String code, String state) throws Exception {
        log.info("code: {}, state: {}", code, state);
        User user = userService.save(code, state);
        if (user != null) {
            log.info("user: {}, 登录成功", user.getLogin());
            if (oauthConfigProperties.getUser().equals(user.getLogin())) {
                log.info("add token to session");
                request.getSession().setAttribute("token", user);
            } else {
                log.info("用户【{}】不是合法得用户,登录后仍然不可操作博客。", user.getLogin());
            }
        }
        response.sendRedirect(state);
    }

    @GetMapping("/getUser")
    public JsonResult getUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("token");
        if (user != null) {
            log.info(user.toString());
            user = userService.getUserByName(oauthConfigProperties.getUser());
            return JsonResult.success(user);
        }
        log.warn("没有登录");
        return JsonResult.fail("没有登录");
    }

    @GetMapping("/getSiteInfo")
    public JsonResult getSiteInfo() {
        return JsonResult.success(siteConfig);
    }

}
