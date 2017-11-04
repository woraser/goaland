package com.anosi.asset.component;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
public class RemoteComponent {

	@Value("${remote.protocol}")
	private String protocol;
	@Value("${remote.host}")
	private String host;
	@Value("${remote.port}")
	private String port;

	/***
	 * 获取完整路径
	 * 
	 * @param servletPath
	 * @return
	 */
	public String getFullPath(String servletPath) {
		return protocol + "://" + host + ":" + port + "/" + servletPath;
	}

	/****
	 * 返回包含sessionId的请求头
	 * 
	 * @return
	 */
	public Map<String, String> getHearders() {
		return ImmutableMap.of("Cookie", "JSESSIONID=" + SessionComponent.getSession().getId());
	}

}
