package com.anosi.asset.util;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {

	private static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);

	public String login(Subject currentUser,String loginId,UsernamePasswordToken token) {
		try {
			// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			logger.info("对用户[" + loginId + "]进行登录验证..验证开始");
			currentUser.login(token);
			logger.info("对用户[" + loginId + "]进行登录验证..验证通过");
			return null;
		} catch (UnknownAccountException uae) {
			logger.info("对用户[" + loginId + "]进行登录验证..验证未通过,未知账户");
			return "未知账户";
		} catch (IncorrectCredentialsException ice) {
			logger.info("对用户[" + loginId + "]进行登录验证..验证未通过,错误的凭证");
			return "密码不正确";
		} catch (LockedAccountException lae) {
			logger.info("对用户[" + loginId + "]进行登录验证..验证未通过,账户已锁定");
			return "账户已锁定";
		} catch (ExcessiveAttemptsException eae) {
			logger.info("对用户[" + loginId + "]进行登录验证..验证未通过,错误次数过多");
			return "用户名或密码错误次数过多";
		} catch (AuthenticationException ae) {
			// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			logger.info("对用户[" + loginId + "]进行登录验证..验证未通过,堆栈轨迹如下");
			ae.printStackTrace();
			return "用户名或密码不正确";
		}

	}
	
}
