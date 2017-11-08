package com.anosi.asset.controller.remote;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.util.URLConncetUtil;

@RestController
public class AlarmDataController extends BaseRemoteController {

	private static final Logger logger = LoggerFactory.getLogger(AlarmDataController.class);

	@Autowired
	private WebSocketComponent webSocketComponent;

	/***
	 * 获取告警数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions({ "iotxAlarmData:view" })
	@RequestMapping(value = "/alarmData/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findAlarmDataManageData(HttpServletRequest request) throws Exception {
		logger.debug("get iotxData");
		String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}

	/***
	 * 发生告警时调用
	 * 
	 * @param alarmData
	 * @throws Exception
	 */
	@RequestMapping(value = "/alarmData/alarm/occur", method = RequestMethod.GET)
	public void alarmOccur(@RequestParam(value = "alarmData") String alarmData) throws Exception {
		logger.debug("alarm occur");
		webSocketComponent.sendByBroadcast("/topic/broadcast/alarmData", alarmData);
	}


	/***
	 * 修改alarmData
	 * 
	 * @param sensor
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/alarmData/save", method = RequestMethod.POST)
	public JSONObject save(HttpServletRequest request) throws Exception {
		logger.debug("saveOrUpdate alarmData");
		String result = URLConncetUtil.sendPostString(remoteComponent.getFullPath(request.getServletPath()),
				request.getParameterMap(), remoteComponent.getHearders());
		return JSON.parseObject(result);
	}
	
}
