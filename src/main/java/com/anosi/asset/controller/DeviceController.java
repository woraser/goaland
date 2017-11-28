package com.anosi.asset.controller;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
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
import com.anosi.asset.model.jpa.QDevice;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.DevCategoryService;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.service.DocumentTypeService;
import com.anosi.asset.util.StringUtil;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class DeviceController extends BaseController<Device> {

	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DevCategoryService devCategorySerivce;
	@Autowired
	private DocumentTypeService documentTypeService;
	@Autowired
	private AccountService accountService;

	/***
	 * 进入查看<b>所有设备信息</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/device/management/view", method = RequestMethod.GET)
	public ModelAndView toViewDeviceManage() {
		logger.debug("view device manage");
		return new ModelAndView("device/deviceManage");
	}

	/***
	 * 根据条件查询某个device
	 * 
	 * @param showType
	 * @param predicate
	 * @param showAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/management/data/one", method = RequestMethod.GET)
	public JSONObject findDeviceManageDataOne(@QuerydslPredicate(root = Device.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes) throws Exception {
		logger.info("find device one");
		return jsonUtil.parseAttributesToJson(StringUtil.splitAttributes(showAttributes),
				deviceService.findOne(predicate));
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
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "searchContent", required = false) String searchContent,
			@RequestParam(value = "beginTime", required = false) Date beginTime,
			@RequestParam(value = "endTime", required = false) Date endTime) throws Exception {
		logger.info("find device");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		Page<Device> devices;
		if (beginTime != null) {
			predicate = QDevice.device.commissioningTime.after(beginTime).and(predicate);
		}
		if (endTime != null) {
			predicate = QDevice.device.commissioningTime.before(endTime).and(predicate);
		}
		if (StringUtils.isNoneBlank(searchContent)) {
			devices = deviceService.findByContentSearch(searchContent, pageable);
		} else {
			devices = deviceService.findAll(predicate, pageable);
		}

		return parseToJson(devices, rowId, showAttributes, showType);
	}

	/***
	 * 进入save/update device的页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/save", method = RequestMethod.GET)
	public ModelAndView toSaveDevicePage(@RequestParam(value = "id", required = false) Long id) throws Exception {
		Device device;
		if (id == null) {
			device = new Device();
		} else {
			device = deviceService.getOne(id);
		}
		return new ModelAndView("device/save").addObject("device", device).addObject("devCategorys",
				devCategorySerivce.findAll());
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

	/***
	 * save/update device
	 * 
	 * @param device
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions({ "deviceManagement:add", "deviceManagement:edit" })
	@RequestMapping(value = "/device/save", method = RequestMethod.POST)
	public JSONObject saveDevice(@ModelAttribute("device") Device device) throws Exception {
		logger.debug("saveOrUpdate device");
		deviceService.save(device);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 删除设备
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions({ "deviceManagement:delete" })
	@RequestMapping(value = "/device/delete", method = RequestMethod.POST)
	public JSONObject deleteDevice(@RequestParam(value = "id") Long id) throws Exception {
		logger.debug("delete device");
		deviceService.delete(id);
		return new JSONObject(ImmutableMap.of("result", "success"));
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
	public JSONArray autocomplete(@QuerydslPredicate(root = Device.class) Predicate predicate,
			@RequestParam(value = "label") String label, String value) throws Exception {
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

	/***
	 * 为设备绑定rifd
	 * 
	 * @param serialNo
	 * @param rfid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/rfid/binding", method = RequestMethod.POST)
	@Transactional
	public JSONObject bindingRfid(@RequestParam(value = "serialNo") String serialNo,
			@RequestParam(value = "rfid") String rfid) throws Exception {
		Device device = deviceService.findBySerialNo(serialNo);
		if (device == null) {
			return new JSONObject(ImmutableMap.of("result", "error", "message",
					MessageFormat.format(i18nComponent.getMessage("device.rfid.exist"), serialNo)));
		} else if (StringUtils.isNoneBlank(device.getRfid())) {
			return new JSONObject(
					ImmutableMap.of("result", "error", "message", i18nComponent.getMessage("device.rfid.exist")));
		} else if (StringUtils.isBlank(rfid)) {
			return new JSONObject(
					ImmutableMap.of("result", "error", "message", i18nComponent.getMessage("rfid.cannot.null")));
		} else if (deviceService.findByRfid(rfid) != null) {
			return new JSONObject(
					ImmutableMap.of("result", "error", "message", i18nComponent.getMessage("rfid.repeat")));
		} else {
			device.setRfid(rfid);
		}
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 为设备解绑rifd
	 * 
	 * @param serialNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/rfid/unBinding", method = RequestMethod.POST)
	@Transactional
	public JSONObject unBindingRfid(@RequestParam(value = "serialNo") String serialNo) throws Exception {
		deviceService.findBySerialNo(serialNo).setRfid(null);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 点击device详情进入的页面
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/device/management/detail/{deviceId}/view", method = RequestMethod.GET)
	public ModelAndView toViewDeviceManageTable(@PathVariable Long deviceId) throws Exception {
		logger.info("view device management detail");
		Device device = deviceService.getOne(deviceId);
		return new ModelAndView("device/detail").addObject("deviceId", deviceId).addObject("serialNo",
				device.getSerialNo());
	}

	/***
	 * 进入文档上传页面
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/device/document/upload/{deviceId}/view", method = RequestMethod.GET)
	public ModelAndView deviceDocumentUpload(@PathVariable Long deviceId) {
		logger.debug("device document upload");
		return new ModelAndView("device/upload").addObject("device", deviceService.getOne(deviceId));
	}

	/***
	 * 进入设备技术文档页面
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/device/technologyDocument/manage/{deviceId}/view", method = RequestMethod.GET)
	public ModelAndView toTechnologyDocumentManage(@PathVariable Long deviceId) {
		logger.debug("technologyDocument manage");
		return new ModelAndView("document/documentManage").addObject("types", documentTypeService.findAll())
				.addObject("uploaders", accountService.findByIsUploadDocument(true))
				.addObject("device", deviceService.getOne(deviceId)).addObject("isDevice", true);
	}

}
