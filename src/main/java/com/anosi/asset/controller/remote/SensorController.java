package com.anosi.asset.controller.remote;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class SensorController extends BaseRemoteController {

	private static final Logger logger = LoggerFactory.getLogger(SensorController.class);

	/***
	 * 进入sensor管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sensor/management/view", method = RequestMethod.GET)
	public ModelAndView toViewSensorManageTable() {
		logger.info("view sensor management");
		return new ModelAndView("sensor/sensorMgr");
	}

	/***
	 * 获取sensor管理的数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sensor/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findSensorManageData(HttpServletRequest request) throws Exception {
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

	/***
	 * 点进传感器查看详情的页面
	 * 
	 * @param sensorId
	 * @return
	 */
	@RequestMapping(value = "/sensor/management/detail/{serialNo}/view", method = RequestMethod.GET)
	public ModelAndView toViewSensorManageTable(@PathVariable String serialNo) {
		logger.info("view sensor management detail");
		return new ModelAndView("sensor/managementDetail").addObject("serialNo", serialNo);
	}

	/***
	 * 根据条件查询某个sensor
	 * 
	 * @param showType
	 * @param predicate
	 * @param showAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sensor/management/data/one", method = RequestMethod.GET)
	public JSONObject findSensorManageDataOne(HttpServletRequest request) throws Exception {
		logger.info("find sensor one");
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

}
