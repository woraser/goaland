package com.anosi.asset.controller.remote;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class IotxController extends BaseRemoteController {

	private static final Logger logger = LoggerFactory.getLogger(IotxController.class);

	/***
	 * 进入iotx管理地图页面
	 * 
	 * @return
	 */
	@RequiresPermissions({ "iotxManagement:view" })
	@RequestMapping(value = "/iotx/management/map/view", method = RequestMethod.GET)
	public ModelAndView toViewIotxManageMap() {
		logger.info("view iotx management map");
		return new ModelAndView("iotx/managementMap");
	}

	/***
	 * 进入iotx管理表格页面
	 * 
	 * @return
	 */
	@RequiresPermissions({ "iotxManagement:view" })
	@RequestMapping(value = "/iotx/management/table/view", method = RequestMethod.GET)
	public ModelAndView toViewIotxManageTable() {
		logger.info("view iotx management table");
		return new ModelAndView("iotx/managementTable");
	}

	/***
	 * 获取iotx管理的数据
	 * 
	 * @param request
	 * @return
	 */
	@RequiresPermissions({ "iotxManagement:view" })
	@RequestMapping(value = "/iotx/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findIotxManageData(HttpServletRequest request) {
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

}
