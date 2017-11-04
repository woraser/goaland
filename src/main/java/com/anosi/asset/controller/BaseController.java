package com.anosi.asset.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.component.SessionComponent;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.service.RoleFunctionService;
import com.anosi.asset.util.DataTablesUtil;
import com.anosi.asset.util.JqgridUtil;
import com.anosi.asset.util.JsonUtil;
import com.anosi.asset.util.StringUtil;

public class BaseController<T> extends GlobalController<T> {

	@Autowired
	protected JqgridUtil<T> jqgridUtil;
	@Autowired
	protected JsonUtil<T> jsonUtil;
	@Autowired
	protected DataTablesUtil<T> dataTablesUtil;
	@Autowired
	protected RoleFunctionService roleFunctionService;
	@Autowired
	protected EntityManager entityManager;
	@Autowired
	protected SessionComponent sessionComponent;
	@Autowired
	protected I18nComponent i18nComponent;

	/***
	 * 注册date
	 * 
	 * @param request
	 * @param binder
	 */
	@InitBinder
	protected void initDate(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/***
	 * 注册double
	 * 
	 * @param request
	 * @param binder
	 */
	@InitBinder
	protected void initDouble(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
	}

	@ModelAttribute("menus")
	public Iterable<RoleFunction> setMenu() {
		return roleFunctionService.findByParentRoleFunctionIsNull();
	}

	/***
	 * 在每个controller调用前，将menuId加入session
	 * 
	 * @param id
	 * @param model
	 */
	@ModelAttribute
	public void setMenuIdIntoSession(@RequestParam(value = "menuId", required = false) String menuId) {
		if (StringUtils.isNoneBlank(menuId)) {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			session.setAttribute("menuId", menuId);
		}
	}

	/****
	 * 根据showType将数据转为指定格式的json
	 * 
	 * @param pages
	 * @param rowId
	 * @param showAttributes
	 * @param showType
	 * @return
	 * @throws Exception
	 */
	protected JSONObject parseToJson(Page<T> pages, String rowId, String showAttributes, ShowType showType)
			throws Exception {
		JSONObject jsonObject = null;
		switch (showType) {
		case GRID:
			jsonObject = jqgridUtil.parsePageToJqgridJson(pages, rowId, StringUtil.splitAttributes(showAttributes));
			break;
		case REMOTE:
			jsonObject = jsonUtil.parseAttributesToJson(pages, StringUtil.splitAttributes(showAttributes));
			break;
		case DATATABLES:
			jsonObject = dataTablesUtil.parsePageToDataTablesJson(pages, rowId,
					StringUtil.splitAttributes(showAttributes));
			break;
		default:
			jsonObject = jsonUtil.parseAttributesToJson(pages, StringUtil.splitAttributes(showAttributes));
		}
		return jsonObject;
	}

	enum ShowType {
		GRID, REMOTE, DATATABLES
	}

}
