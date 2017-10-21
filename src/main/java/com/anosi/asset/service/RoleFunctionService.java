package com.anosi.asset.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionBtn;

public interface RoleFunctionService extends BaseJPAService<RoleFunction>{

	public RoleFunction findByRoleFunctionPageId(String roleFunctionPageId);
	
	public List<RoleFunction> findByParentRoleFunctionIsNull();

	/***
	 * 将菜单权限转化为zTree字符串
	 * @return
	 */
	public JSONArray parseToTree(List<RoleFunction> accountRoleFunctionList, List<RoleFunctionBtn> accountRoleFunctionBtnList);
	
}
