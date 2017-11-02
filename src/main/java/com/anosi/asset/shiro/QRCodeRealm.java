package com.anosi.asset.shiro;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class QRCodeRealm extends CustomRealm {

	private static final Logger logger = LoggerFactory.getLogger(QRCodeRealm.class);

	@Autowired
	private SessionRedisDao sessionRedisDao;

	/**
	 * 认证信息.(身份验证) Authentication 是用来验证用户身份
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		// UsernamePasswordToken对象用来存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

		logger.info(
				"验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

		String loginId = token.getUsername();// sessionId
		Session session = sessionRedisDao.doReadSession(new String(token.getPassword()));
		if (session != null) {
			// 交给AuthenticatingRealm进行匹配
			return new SimpleAuthenticationInfo(loginId, session.getId(), getName());
		}
		return null;
	}

}
