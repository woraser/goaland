package com.anosi.asset.controller;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.service.DeviceService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class DeviceController extends BaseController<Device> {

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
	 * 
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

	/****
	 * 在执行update前，先获取持久化的device对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getDevice(@RequestParam(value = "deviceId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("device", deviceService.getOne(id));
		}
	}

	/**
	 * 为设备添加坐标
	 * 
	 * @param deviceSN
	 * @param longitude
	 * @param latitude
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/setDistrict", method = RequestMethod.POST)
	public JSONObject setDeviceDistrict(@RequestParam(name = "deviceSN") String deviceSN,
			@RequestParam(name = "longitude") Double longitude, @RequestParam(name = "latitude") Double latitude)
			throws Exception {
		Device device = deviceService.findBySerialNo(deviceSN);
		if (device == null) {
			return new JSONObject(ImmutableMap.of("result", "error", "message",
					MessageFormat.format(i18nComponent.getMessage("device.notExist.withSN"), deviceSN)));
		} else {
			device.setLongitude(longitude);
			device.setLatitude(latitude);
			deviceService.setDeviceDistrict(device);
		}
		return new JSONObject(ImmutableMap.of("result", "success"));
	}
	
	/***
	 * 获取设备的分布
	 * 
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/distribute/data", method = RequestMethod.GET)
	public JSONArray deviceDistribute(@QuerydslPredicate(root = Device.class) Predicate predicate) throws Exception {
		return deviceService.ascertainArea(predicate);
	}
	
	/***
	 * 获取autocomplete的source
	 * 
	 * @param predicate
	 * @param label
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/autocomplete", method = RequestMethod.GET)
	public JSONArray autocomplete(@QuerydslPredicate(root = Device.class) Predicate predicate, @RequestParam(value = "label") String label,
			String value) throws Exception {
		return jsonUtil.parseAttributesToAutocomplete(label, value, deviceService.findAll(predicate));
	}
	
	/**
	 * 按照device某些属性判断是否存在
	 * 
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/checkExist", method = RequestMethod.GET)
	public JSONObject checkExist(@QuerydslPredicate(root = Device.class) Predicate predicate) throws Exception {
		return new JSONObject(ImmutableMap.of("result", deviceService.exists(predicate)));
	}
	
}
