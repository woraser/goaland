package com.anosi.asset.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;

@Component
@Transactional
public class SessionComponent {

	@Autowired
	private AccountService accountService;

	public static Session getSession() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		return session;
	}

	public Account getCurrentUser() {
		Session session = getSession();
		String loginId = (String) session.getAttribute("loginId");
		if (StringUtils.isNoneBlank(loginId)) {
			return accountService.findByLoginId(loginId);
		}
		return null;
	}

	public static boolean isAdmin() {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.hasRole("admin");
	}
	
	public static boolean isClient() {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.hasRole("client");
	}

}
