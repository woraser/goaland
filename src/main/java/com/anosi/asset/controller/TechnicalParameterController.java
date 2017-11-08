package com.anosi.asset.controller;

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
import com.anosi.asset.model.jpa.TechnicalParameter;
import com.anosi.asset.service.TechnicalParameterService;
import com.querydsl.core.types.Predicate;

@RestController
public class TechnicalParameterController extends BaseController<TechnicalParameter>{

	@Autowired
	private TechnicalParameterService technicalParameterService;
	
	/***
	 * 获取技术参数的元数据
	 * 可以通过控制predicate来完成某一设备的技术参数的查询
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/technicalParameter/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findTechnicalParameterData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = TechnicalParameter.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception{
		
		return parseToJson(technicalParameterService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}
	
}
