package com.anosi.asset.controller.remote;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.anosi.asset.component.SessionComponent;
import com.anosi.asset.controller.BaseController;
import com.anosi.asset.model.jpa.BaseEntity;
import com.google.common.collect.ImmutableMap;

@RestController
public class BaseRemoteController extends BaseController<BaseEntity> {

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
	protected String getFullPath(String servletPath) {
		return protocol + "://" + host + ":" + port + "/" + servletPath;
	}

	/****
	 * 返回包含sessionId的请求头
	 * 
	 * @return
	 */
	protected Map<String, String> getHearders() {
		return ImmutableMap.of("Cookie", "JSESSIONID=" + SessionComponent.getSession().getId());
	}

}
