package com.anosi.asset.controller;

import java.util.Date;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.CustomerServiceProcessDailyPer;
import com.anosi.asset.service.CustomerServiceProcessDailyPerService;
import com.querydsl.core.types.Predicate;

@RestController
public class CustomerServiceProcessDailyPerController extends BaseController<CustomerServiceProcessDailyPer> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProcessDailyPerController.class);

	@Autowired
	private CustomerServiceProcessDailyPerService customerServiceProcessDailyPerService;

	/***
	 * 获取未完成的工单数和已完成的工单数
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerServiceProcessDailyPer/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findIotxManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = CustomerServiceProcessDailyPer.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes) throws Exception {
		logger.info("find customerServiceProcessDailyPer");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

		return parseToJson(customerServiceProcessDailyPerService.findAll(predicate, pageable), null, showAttributes,
				showType);
	}
	
	public JSONArray getCountArray(@RequestParam(value = "beginTime", required = false) Date beginTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agreement", required = false) String agreement,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable) throws Exception{
		return null;
	}
	
}
