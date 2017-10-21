package com.anosi.asset.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RoleFunctionDao;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionBtn;
import com.anosi.asset.service.RoleFunctionService;

@Service("roleFunctionService")
@Transactional
public class RoleFunctionServiceImpl extends BaseJPAServiceImpl<RoleFunction> implements RoleFunctionService {

	@Autowired
	private RoleFunctionDao roleFunctionDao;

	@Override
	public RoleFunction findByRoleFunctionPageId(String roleFunctionPageId) {
		return roleFunctionDao.findByRoleFunctionPageIdEquals(roleFunctionPageId);
	}

	@Override
	public BaseJPADao<RoleFunction> getRepository() {
		return roleFunctionDao;
	}

	@Override
	public List<RoleFunction> findByParentRoleFunctionIsNull() {
		return roleFunctionDao.findByParentRoleFunctionIsNull();
	}

	@Override
	public JSONArray parseToTree(List<RoleFunction> accountRoleFunctionList, List<RoleFunctionBtn> accountRoleFunctionBtnList) {
		JSONArray mainArray = new JSONArray();
		JSONObject rootJson = new JSONObject();
		rootJson.put("id", "menu_0");
		rootJson.put("pId", "-1");
		rootJson.put("name", "全部");
		rootJson.put("icon", "/webResources/plugins/ztree/img/diy/3.png");
		rootJson.put("open", "true");
		mainArray.add(rootJson);

		// 查找出所有父节点
		List<RoleFunction> roleFunctionList = this.roleFunctionDao.findByParentRoleFunctionIsNull();
		for (RoleFunction roleFunction : roleFunctionList) {
			setChildRoleFunction(roleFunction, mainArray, rootJson, accountRoleFunctionList,
					accountRoleFunctionBtnList);
		}
		return mainArray;
	}

	/****
	 * 设置tree的子节点属性
	 * 
	 * @param roleFunction
	 * @param mainArray
	 * @param rootJson
	 * @param accountRoleFunctionList
	 * @param accountRoleFunctionBtnList
	 */
	private void setChildRoleFunction(RoleFunction roleFunction, JSONArray mainArray, JSONObject rootJson,
			List<RoleFunction> accountRoleFunctionList, List<RoleFunctionBtn> accountRoleFunctionBtnList) {
		JSONObject roleFunctionJson = new JSONObject();
		// 设置菜单属性
		roleFunctionJson.put("id", "menu_" + String.valueOf(roleFunction.getId()));
		roleFunctionJson.put("pId", "menu_" + (null == roleFunction.getParentRoleFunction() ? "0"
				: String.valueOf(roleFunction.getParentRoleFunction().getId())));
		roleFunctionJson.put("name", roleFunction.getName());
		roleFunctionJson.put("icon", "/webResources/plugins/ztree/img/diy/9.png");
		mainArray.add(roleFunctionJson);
		if (accountRoleFunctionList.contains(roleFunction)) {
			parentCheck(mainArray, roleFunction);
			rootJson.put("checked", "true");
		}
		for (RoleFunction rf : roleFunction.getSubRoleFunction()) {
			// 递归设置
			setChildRoleFunction(rf, mainArray, rootJson, accountRoleFunctionList, accountRoleFunctionBtnList);
		}
		if (roleFunction.getSubRoleFunction().size() == 0) {
			// 设置按钮属性
			for (RoleFunctionBtn btn : roleFunction.getRoleFunctionBtnList()) {
				JSONObject roleFunctionBtnJson = new JSONObject();
				roleFunctionBtnJson.put("id", String.valueOf(btn.getId()));
				roleFunctionBtnJson.put("pId", "menu_" + String.valueOf(roleFunction.getId()));
				roleFunctionBtnJson.put("name", btn.getName());
				roleFunctionBtnJson.put("icon", "/webResources/plugins/ztree/img/diy/2.png");
				if (accountRoleFunctionBtnList.contains(btn)) {
					roleFunctionBtnJson.put("checked", "true");
				}
				mainArray.add(roleFunctionBtnJson);
			}
		}
	}

	/***
	 * 如果有子节点被选中，那么父节点也设置选中
	 * @param mainArray
	 * @param roleFunction
	 */
	private void parentCheck(JSONArray mainArray, RoleFunction roleFunction) {
		for (int i = 0; i < mainArray.size(); i++) {
			JSONObject jsonObject = mainArray.getJSONObject(i);
			if (jsonObject.get("id").equals("menu_" +roleFunction.getId())) {
				jsonObject.put("checked", "true");
			}
		}
		if (roleFunction.getParentRoleFunction() != null) {
			parentCheck(mainArray, roleFunction.getParentRoleFunction());
		}

	}
}
