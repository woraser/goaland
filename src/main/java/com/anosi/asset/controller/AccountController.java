package com.anosi.asset.controller;

import java.util.stream.Collectors;

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
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.Role;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionGroup;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.RoleFunctionGroupService;
import com.anosi.asset.service.RoleService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountController extends BaseController<Account> {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleFunctionGroupService roleFunctionGroupService;

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
	 * 
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
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "searchContent", required = false) String searchContent) throws Exception {
		logger.info("find account");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		Page<Account> accounts;
		if (StringUtils.isNoneBlank(searchContent)) {
			accounts = accountService.findByContentSearch(searchContent, pageable);
		} else {
			accounts = accountService.findAll(predicate, pageable);
		}

		return parseToJson(accounts, rowId, showAttributes, showType);
	}

	/***
	 * 进入添加用户的页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/save", method = RequestMethod.GET)
	public ModelAndView toSaveAccountPage(@RequestParam(value = "id", required = false) Long id) throws Exception {
		Account account = null;
		ModelAndView mv = new ModelAndView("account/save");
		if (id != null) {
			account = accountService.getOne(id);
			/*
			 * // 获得当前用户部门下的所有角色 roles =
			 * account.getDepartment().getDepGroupList().stream().map(depGroup
			 * -> depGroup.getRoleList()).collect( () -> new ArrayList<Role>(),
			 * (list, item) -> list.addAll(item), (list1, list2) ->
			 * list1.addAll(list2));
			 */
			mv.addObject("roleIds", account.getRoleList().stream().map(Role::getId).collect(Collectors.toList()));
			mv.addObject("groupIds", account.getRoleFunctionGroupList().stream().map(RoleFunctionGroup::getId)
					.collect(Collectors.toList()));
		} else {
			account = new Account();
		}
		return mv.addObject("account", account).addObject("roles", roleService.findAll())
				.addObject("roleFunctionGroups", roleFunctionGroupService.findAll());
	}

	/***
	 * 将菜单权限转化为zTree字符串
	 * 
	 * @return
	 */
	@RequestMapping(value = "/account/roleFunction/tree/data", method = RequestMethod.GET)
	public JSONArray parseToTree(@RequestParam(name = "accountId", required = false) Long id) {
		return accountService.parseRoleFunctionToTree(id);
	}

	/****
	 * 在执行update前，先获取持久化的account对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getAccount(@RequestParam(value = "accountId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("account", accountService.getOne(id));
		}
	}

	/***
	 * 添加或者修改account
	 * 
	 * @param account
	 * @param password
	 *            如果请求中传来了password,说明修改了密码或者新建了用户
	 * @param selRolesFunctionNode
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequiresPermissions({ "employeeMgr:add", "employeeMgr:edit" })
	@RequestMapping(value = "/account/save", method = RequestMethod.POST)
	public JSONObject save(@ModelAttribute("account") Account account,
			@RequestParam(name = "newPassword", required = false) String password,
			@RequestParam(name = "role") String[] roles,
			@RequestParam(name = "roleFunctionGroup", required = false) String[] roleFunctionGroups,
			String[] selRolesFunctionNode) throws Exception {
		logger.debug("saveOrUpdate account");
		accountService.save(account, password, roles, roleFunctionGroups, selRolesFunctionNode);
		return new JSONObject(ImmutableMap.of("result", "success"));
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
	@RequestMapping(value = "/account/autocomplete", method = RequestMethod.GET)
	public JSONArray autocomplete(@QuerydslPredicate(root = Account.class) Predicate predicate,
			@RequestParam(value = "label") String label, String value) throws Exception {
		return jsonUtil.parseAttributesToAutocomplete(label, value, accountService.findAll(predicate));
	}

	/**
	 * 按照account某些属性判断是否存在
	 * 
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/checkExist", method = RequestMethod.GET)
	public JSONObject checkExist(@QuerydslPredicate(root = Account.class) Predicate predicate) throws Exception {
		return new JSONObject(ImmutableMap.of("result", accountService.exists(predicate)));
	}

	/***
	 * 判断是否是depCode这个部门的人
	 * 
	 * @param depCode
	 * @return
	 */
	@RequestMapping(value = "/account/checkDep", method = RequestMethod.GET)
	public JSONObject checkDep(@RequestParam("depCode") String depCode) {
		return new JSONObject(ImmutableMap.of("result", "success", "dep",
				sessionComponent.getCurrentUser().getDepartment().getCode().equals(depCode)));
	}

	/***
	 * 获取当前用户的权限
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/permission", method = RequestMethod.GET)
	public JSONObject getPermission() throws Exception {
		Account account = sessionComponent.getCurrentUser();
		JSONObject jsonObject = new JSONObject();
		JSONArray roleArray = new JSONArray();
		JSONArray permissionArray = new JSONArray();

		account.getRoleList().forEach(role -> roleArray.add(role.getCode()));// 添加角色
		// 添加权限
		account.getPrivilegeList().forEach(privilege -> {
			RoleFunction roleFunction = privilege.getRoleFunction();
			// 默认添加view权限
			permissionArray.add(roleFunction.getRoleFunctionPageId() + ":view");
			// 添加详细的权限
			privilege.getRoleFunctionBtnList()
					.forEach(btn -> permissionArray.add(roleFunction.getRoleFunctionPageId() + ":" + btn.getBtnId()));
		});
		jsonObject.put("role", roleArray);
		jsonObject.put("permission", permissionArray);
		return jsonObject;
	}

}
