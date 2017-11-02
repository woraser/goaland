package com.anosi.asset.shiro;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anosi.asset.model.jpa.Account;

public class TokenRealm extends CustomRealm{
	
	private static final Logger logger = LoggerFactory.getLogger(TokenRealm.class);
	
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

		Account account = accountService.findByLoginId(token.getUsername());
		if (account != null) {
			// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
			return new SimpleAuthenticationInfo(account.getLoginId(), account.getPassword(),
					ByteSource.Util.bytes(account.getCredentialsSalt()), getName());
		}
		return null;
	}

}
