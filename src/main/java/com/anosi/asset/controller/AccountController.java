package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.util.JqgridUtil;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountController extends BaseController<Account>{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private JqgridUtil<Account> jqgridUtil;
	
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
	@RequestMapping(value = "/account/management/data", method = RequestMethod.GET)
	public JSONObject findAccountManageData(
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Account.class) Predicate predicate,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
					throws Exception {
		logger.info("find iotx");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);
		
		return jqgridUtil.parsePageToJqgridJson(accountService.findAll(predicate, pageable), rowId,
				showAttributes.split(","));
	}
	
}
