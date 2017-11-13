package com.anosi.asset.controller.remote;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class IotxStatusPerController extends BaseRemoteController{

	/***
	 * 获取iotxStatusPer管理的数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iotxStatusPer/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findIotxManageData(HttpServletRequest request) throws Exception {
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}
	
}
