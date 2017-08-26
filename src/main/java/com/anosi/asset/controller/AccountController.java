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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountController extends BaseController<Account>{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	/***
	 * 获取account数据
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findAccountManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Account.class) Predicate predicate,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
					throws Exception {
		logger.info("find iotx");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);
		
		return parseToJson(accountService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}
	
	/***
	 * 获取autocomplete的source
	 * @param predicate
	 * @param label
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/autocomplete", method = RequestMethod.GET)
	public JSONArray autocomplete(@QuerydslPredicate(root = Account.class) Predicate predicate,@RequestParam(value = "label") String label,
			String value) throws Exception{
		return jsonUtil.parseAttributesToAutocomplete(label, value, accountService.findAll(predicate));
	} 
	
}
