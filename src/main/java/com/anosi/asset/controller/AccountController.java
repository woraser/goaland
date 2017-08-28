package com.anosi.asset.controller;

import org.apache.commons.lang3.StringUtils;
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
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.RoleService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountController extends BaseController<Account>{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	
	/***
	 * 进入查看<b>用户信息管理</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/account/management/view", method = RequestMethod.GET)
	public ModelAndView toViewAccountManage() {
		logger.debug("view account manage");
		return new ModelAndView("account/accountManage");
	}
	
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
	 * 进入添加用户的页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/save", method = RequestMethod.GET)
	public ModelAndView toSaveIotxPage(@RequestParam(value = "id", required = false) Long id) throws Exception{
		Account account = null;
		if (id != null) {
			account = accountService.getOne(id);
		} else {
			account = new Account();
		}
		return new ModelAndView("account/save").addObject("account", account).addObject("roles", roleService.findAll());
	}
	
	/****
	 * 在执行update前，先获取持久化的account对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getIox(@RequestParam(value = "accountId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("account", accountService.getOne(id));
		}
	}
	
	/***
	 * 添加或者修改account
	 * @param account
	 * @param 如果请求中传来了password,说明修改了密码或者新建了用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/save", method = RequestMethod.POST)
	public JSONObject saveIotx(@ModelAttribute("account") Account account,String password) throws Exception {
		logger.debug("saveOrUpdate account");
		if(StringUtils.isBlank(password)){
			accountService.save(account);
		}else{
			accountService.save(account,password);
		}
		return new JSONObject(ImmutableMap.of("result", "success"));
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
	
	/**
	 * 按照account某些属性判断是否存在
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/checkExist", method = RequestMethod.GET)
	public JSONObject checkExist(@QuerydslPredicate(root = Account.class) Predicate predicate) throws Exception{
		return new JSONObject(ImmutableMap.of("result", accountService.exists(predicate)));
	}
	
}
