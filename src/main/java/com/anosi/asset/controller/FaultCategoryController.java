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

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.FaultCategory;
import com.anosi.asset.service.FaultCategoryService;
import com.querydsl.core.types.Predicate;

@RestController
public class FaultCategoryController extends BaseController<FaultCategory>{
	
	private static final Logger logger = LoggerFactory.getLogger(FaultCategoryController.class);

	@Autowired
	private FaultCategoryService faultCategoryService;
	

	/***
	 * 获取故障分类数据
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/faultCategory/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findFaultCategoryManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Device.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find device");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(faultCategoryService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}
	
}
