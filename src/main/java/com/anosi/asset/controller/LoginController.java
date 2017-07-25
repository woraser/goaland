package com.anosi.asset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
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

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.util.LoginUtil;

@RestController
public class LoginController extends BaseController<Account> {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private LoginUtil loginUtil;

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
	public ModelAndView login(HttpServletRequest request, @Valid Account account,RedirectAttributes redirectAttributes,boolean rememberMe) throws Exception {
		logger.debug("...login...");
		
		String loginId = account.getLoginId();
		
		//获取当前的Subject  
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginId, account.getPassword(),rememberMe);
        //登录验证
        String result = loginUtil.login(currentUser,loginId,token);
        if(StringUtils.isNoneBlank(result)){
        	redirectAttributes.addFlashAttribute("message",result);
        }
       
        //验证是否登录成功  
        if(currentUser.isAuthenticated()){  
            logger.info("用户[" + loginId + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)"); 
            //使用shiro提供的session
            Session session = currentUser.getSession();
            session.setAttribute("user",  this.accountService.findByLoginId(loginId));
            
            return new ModelAndView("redirect:/index");
        }else{  
            token.clear();  
            return new ModelAndView("redirect:/login");
        }  
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
