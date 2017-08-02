package com.anosi.asset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.LoginComponent;
import com.anosi.asset.model.jpa.Account;

@RestController
public class LoginController extends BaseController<Account> {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginComponent loginComponent;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginForm(Model model) {
		return new ModelAndView("login","model",model);
	}

	/***
	 * 登录提交地址和shiro配置文件中配置的loginurl一致
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, Account account,RedirectAttributes redirectAttributes,boolean rememberMe) throws Exception {
		logger.debug("...login...");
		
        String result=loginComponent.login(account, rememberMe);
        if(result=="success"){
        	return new ModelAndView("redirect:/index");
        }else{
        	redirectAttributes.addFlashAttribute("message",result);
        	return new ModelAndView("redirect:/login");
        }
	}
	
	/***
	 * 移动端登录
	 * @param request
	 * @param account
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/remote", method = RequestMethod.POST)
	public JSONObject loginRemote(HttpServletRequest request, Account account) throws Exception {
		logger.debug("...remote login...");
		
		JSONObject jsonObject = new JSONObject();
		String result=loginComponent.login(account, false);
        if(result=="success"){
        	jsonObject.put("result", "success");
        }else{
        	jsonObject.put("result", "error");
        	jsonObject.put("message", result);
        }
		return jsonObject;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取当前的Subject  
        Subject currentUser = SecurityUtils.getSubject();  
        currentUser.logout();
        return new ModelAndView("redirect:/login");
	}
	
}
