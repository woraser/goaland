package com.anosi.asset.controller.remote;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class IotxDataController extends BaseRemoteController {

	private static final Logger logger = LoggerFactory.getLogger(IotxDataController.class);
	
	/***
	 * 获取iotx采集的数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iotxData/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findIotxDataManageData(HttpServletRequest request) throws Exception {
		logger.debug("get iotxData");
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

	/***
	 * 获取线图数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iotxData/dynamicData", method = RequestMethod.GET)
	public JSONObject dynamicData(HttpServletRequest request) throws Exception {
		logger.info("find dynamicData");
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

}
