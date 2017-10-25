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
	
	@RequestMapping(value = "/devCategory/count", method = RequestMethod.GET)
	public JSONArray countByDevCategory() throws Exception {
		return devCategoryService.countByDevCategory();
	}
	
}
