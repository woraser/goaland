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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class IotxController extends BaseRemoteController {

	private static final Logger logger = LoggerFactory.getLogger(IotxController.class);

	/***
	 * 进入iotx管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/iotx/management/view", method = RequestMethod.GET)
	public ModelAndView toViewIotxManageTable() {
		logger.info("view iotx management");
		return new ModelAndView("iotx/iotxMgr");
	}

	/***
	 * 获取iotx管理的数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/iotx/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findIotxManageData(HttpServletRequest request) throws Exception{
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

	/****
	 * 获取iotx分布的数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iotx/iotxDistribute/data", method = RequestMethod.GET)
	public JSONArray iotxDistribute(HttpServletRequest request) throws Exception {
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseArray(result);
	}
	
	/***
	 * 点击iotx详情进入的页面
	 * 
	 * @param iotxId
	 * @return
	 */
	@RequestMapping(value = "/iotx/management/detail/{iotxId}/view", method = RequestMethod.GET)
	public ModelAndView toViewIotxManageTable(@PathVariable Long iotxId) throws Exception {
		logger.info("view iotx management detail");
		return new ModelAndView("iotx/managementDetail").addObject("iotxId", iotxId);
	}
	
	/***
	 * 根据条件查询某个iox
	 * 
	 * @param showType
	 * @param predicate
	 * @param showAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iotx/management/data/one", method = RequestMethod.GET)
	public JSONObject findIotxManageDataOne(HttpServletRequest request) throws Exception {
		logger.info("find iotx one");
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

}
