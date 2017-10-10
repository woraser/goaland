package com.anosi.asset.component;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.anosi.asset.model.jpa.Account;

public class SessionUtil {

	public static Session getSession() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		return session;
	}

	public static Account getCurrentUser() {
		Session session = getSession();
		Object user = session.getAttribute("user");
		if (user != null) {
			return (Account) user;
		} else {
			return null;
		}
	}

}
