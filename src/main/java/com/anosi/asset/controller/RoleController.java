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
import com.anosi.asset.model.jpa.Role;
import com.anosi.asset.service.RoleService;
import com.querydsl.core.types.Predicate;

@RestController
public class RoleController extends BaseController<Role>{

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private RoleService roleService;
	
	/***
	 * 获取条件获得角色数据
	 * 
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/role/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findRoleManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Role.class) Predicate predicate,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find roleFunctionGroup");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(roleService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}
	
}
