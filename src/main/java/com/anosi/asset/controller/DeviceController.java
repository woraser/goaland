package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.service.DeviceService;
import com.querydsl.core.types.Predicate;

@RestController
public class DeviceController extends BaseController<Device>{
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	private DeviceService deviceService;
	
	/***
	 * 进入查看<b>所有设备信息</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/device/management/view", method = RequestMethod.GET)
	public ModelAndView toViewDeviceManage() {
		logger.debug("view account manage");
		return new ModelAndView("device/deviceManage");
	}
	
	/***
	 * 获取设备数据
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findDeviceManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Device.class) Predicate predicate,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find device");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(deviceService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}
	
}
