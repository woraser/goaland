package com.anosi.asset.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;

/***
 * 如果使用了shiro的rememberMe功能，在未登录情况下，服务器端可能没有session，这个filter是为了在这种情况下创建session
 * 
 * @author jinyao
 *
 */
public class AddPrincipalToSessionFilter extends OncePerRequestFilter {

	@Autowired
	private AccountService accountService;

	@Override
	protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isRemembered()) {
			String loginId = subject.getPrincipal().toString();
			Account user = accountService.findByLoginId(loginId);
			if (user == null)
				return;
			Session session = subject.getSession(true);
			session.setAttribute("user", user);
		}
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
