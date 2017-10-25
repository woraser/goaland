package com.anosi.asset.auditorAware;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		Subject currentUser = null;
		try {
			currentUser = SecurityUtils.getSubject();
		} catch (UnavailableSecurityManagerException e) {
			// 项目还未完全启动，就进行初始化的时候会报这个异常
			return null;
		}
		Session session = currentUser.getSession();
		if (session == null) {
			return null;
		} else if (StringUtils.isBlank((String) session.getAttribute("loginId"))) {
			return null;
		} else {
			return (String) session.getAttribute("loginId");
		}
	}
}
