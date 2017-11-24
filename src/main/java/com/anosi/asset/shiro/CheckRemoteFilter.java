package com.anosi.asset.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

/***
 * 判断是移动端的请求还是pc端的请求
 * 
 * @author jinyao
 *
 */
public class CheckRemoteFilter extends OncePerRequestFilter {
	
	public static ThreadLocal<Boolean> isMobile = new ThreadLocal<>();
	
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String user_agent = httpServletRequest.getHeader("User-Agent");
		isMobile.set(isMobileDevice(user_agent));
		chain.doFilter(request, response);
	}

	/**
	 * android : 所有android设备 mac os : iphone ipad windows
	 * phone:Nokia等windows系统的手机
	 */
	public static boolean isMobileDevice(String requestHeader) {
		String[] deviceArray = new String[] { "android", "ios", "mac os", "windows phone" };
		if (requestHeader == null) {
			return false;
		}
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}

}
