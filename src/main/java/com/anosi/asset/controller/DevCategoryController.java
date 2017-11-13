package com.anosi.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.service.DevCategoryService;

@RestController
public class DevCategoryController extends BaseController<DevCategory>{

	@Autowired
	private DevCategoryService devCategoryService;
	
	/***
	 * 获取各种类设备数量的统计结果，设备种类名称有中英文
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/devCategory/count", method = RequestMethod.GET)
	public JSONArray countByDevCategory() throws Exception {
		return devCategoryService.countByDevCategory();
	}
	
	/***
	 * 获取各种类设备数量的统计结果，设备种类名称可在前台转为i18n
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/devCategory/count/i18n", method = RequestMethod.GET)
	public JSONArray countByDevCategoryI18n() throws Exception {
		return devCategoryService.countByDevCategoryI18n();
	}
	
}
