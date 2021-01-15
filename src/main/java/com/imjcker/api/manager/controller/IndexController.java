package com.imjcker.api.manager.controller;

import com.imjcker.api.manager.service.impl.LoginService;
import com.imjcker.api.manager.controller.annotation.PermissionLimit;
import com.imjcker.api.manager.core.model.ReturnT;
import com.imjcker.api.manager.core.model.XxlApiUser;
import com.imjcker.api.manager.core.util.tool.StringTool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * index controller
 * @author imjcker 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	@Resource
	private LoginService loginService;

	@RequestMapping("/")
	@PermissionLimit(limit=false)
	public String index(Model model, HttpServletRequest request) {
		XxlApiUser loginUser = loginService.ifLogin(request);
		if (loginUser == null) {
			return "redirect:/toLogin";
		}
		return "redirect:/project";
	}
	
	@RequestMapping("/toLogin")
	@PermissionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		XxlApiUser loginUser = loginService.ifLogin(request);
		if (loginUser != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermissionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String ifRemember, String userName, String password){
		// param
		boolean ifRem = false;
		if (StringTool.isNotBlank(ifRemember) && "on".equals(ifRemember)) {
			ifRem = true;
		}

		// do login
		ReturnT<String> loginRet = loginService.login(response, userName, password, ifRem);
		return loginRet;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermissionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		loginService.logout(request, response);
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/help")
	public String help() {
		return "help";
	}
	
}
