package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.anosi.asset.model.jpa.RoleFunctionGroup;
import com.anosi.asset.service.RoleFunctionGroupService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class RoleFunctionGroupController extends BaseController<RoleFunctionGroup> {

	private static final Logger logger = LoggerFactory.getLogger(RoleFunctionGroupController.class);

	@Autowired
	private RoleFunctionGroupService roleFunctionGroupService;

	/***
	 * 进入查看<b>权限组</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/roleFunctionGroup/management/view", method = RequestMethod.GET)
	public ModelAndView toViewAccountManage() {
		logger.debug("view roleFunctionGroup manage");
		return new ModelAndView("roleFunctionGroup/roleFunctionGroupManage");
	}

	/***
	 * 获取权限组数据
	 * 
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @param companyCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleFunctionGroup/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findRoleFunctionGroupManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = RoleFunctionGroup.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find roleFunctionGroup");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(roleFunctionGroupService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}

	/***
	 * 进入添加权限组的页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleFunctionGroup/save", method = RequestMethod.GET)
	public ModelAndView toSaveRoleFunctionGroupPage(@RequestParam(value = "id", required = false) Long id)
			throws Exception {
		RoleFunctionGroup roleFunctionGroup = null;
		if (id != null) {
			roleFunctionGroup = roleFunctionGroupService.getOne(id);
		} else {
			roleFunctionGroup = new RoleFunctionGroup();
		}
		return new ModelAndView("roleFunctionGroup/save").addObject("roleFunctionGroup", roleFunctionGroup);
	}

	/***
	 * 将菜单权限转化为zTree字符串
	 * 
	 * @return
	 */
	@RequestMapping(value = "/roleFunctionGroup/roleFunction/tree/data", method = RequestMethod.GET)
	public JSONArray parseToTree(@RequestParam(name = "roleFunctionGroupId", required = false) Long id) {
		return roleFunctionGroupService.parseRoleFunctionToTree(id);
	}

	/****
	 * 在执行update前，先获取持久化的roleFunctionGroup对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getIox(@RequestParam(value = "roleFunctionGroupId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("roleFunctionGroup", roleFunctionGroupService.getOne(id));
		}
	}

	/***
	 * 添加或者修改roleFunctionGroup
	 * 
	 * @param account
	 * @param selRolesFunctionNode
	 *            勾选权限的字符串
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleFunctionGroup/save", method = RequestMethod.POST)
	@Transactional
	public JSONObject saveRoleFunctionGroup(@ModelAttribute("roleFunctionGroup") RoleFunctionGroup roleFunctionGroup,
			String[] selRolesFunctionNode) throws Exception {
		logger.debug("saveOrUpdate roleFunctionGroup");
		roleFunctionGroupService.save(roleFunctionGroup, selRolesFunctionNode);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/****
	 * 删除权限组
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleFunctionGroup/delete", method = RequestMethod.POST)
	public JSONObject deleteRoleFunctionGroup(@RequestParam(value = "id") Long id) throws Exception {
		logger.debug("delete roleFunctionGroup");
		roleFunctionGroupService.delete(id);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/**
	 * 按照roleFunctionGroup某些属性判断是否存在
	 * 
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleFunctionGroup/checkExist", method = RequestMethod.GET)
	public JSONObject checkExist(@QuerydslPredicate(root = RoleFunctionGroup.class) Predicate predicate)
			throws Exception {
		return new JSONObject(ImmutableMap.of("result", roleFunctionGroupService.exists(predicate)));
	}

	/***
	 * 获取到取消roleFunctionGroup时，zTree中需要减少的点
	 * 
	 * @param groupId 点击的groupId
	 * @param allSelectedGroup 已经选中的groupId,
	 * 						空格分隔
	 * @param allSelectedId	所有选中的权限id,
	 * 						逗号分隔
	 * @return
	 */
	@RequestMapping(value = "/roleFunctionGroup/zTree/decrease", method = RequestMethod.GET)
	public JSONObject zTreeDecreaseData(@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "allSelectedGroup", required = false) String allSelectedGroup,
			@RequestParam(value = "allSelectedId", required = false) String allSelectedId) {
		return roleFunctionGroupService.getZTreeDecreaseData(groupId,allSelectedGroup.split(" "),allSelectedId.split(","));
	}

}
