package com.imjcker.manager.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SystemController {
    @GetMapping("/")
    public String index() {
        return "/static/index.html";
    }

    @RequestMapping("/getCasUser")
    @ResponseBody
    public Map<String, Object> getCasUser() {
        Map<String, Object> map = new HashMap<>();

        Map<String, String> principal = new HashMap<>();
        principal.put("name", "name");
        principal.put("realName", "Admin");
        principal.put("loginNo", "admin");
        principal.put("uid", "admin");
        map.put("principal", principal);

        map.put("loginUrl", "serverUrl");
        map.put("logoutUrl", "aaaa");

        map.put("perms", "a,b,c,d".split(","));
        map.put("roles", "admin,network_all_role".split(","));

        return map;
    }

}
