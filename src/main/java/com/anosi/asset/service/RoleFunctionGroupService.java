package com.anosi.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.RoleFunctionGroup;

public interface RoleFunctionGroupService extends BaseJPAService<RoleFunctionGroup> {

	/***
	 * 将菜单权限转化为zTree字符串
	 * 
	 * @param id
	 *            roleFunctionGroup的id
	 * @return
	 */
	JSONArray parseRoleFunctionToTree(Long id);

	/***
	 * save roleFunctionGroup 更新时会更新相关用户的权限
	 * 
	 * @param roleFunctionGroup
	 * @param selRolesFunctionNode
	 * @return
	 */
	RoleFunctionGroup save(RoleFunctionGroup roleFunctionGroup, String[] selRolesFunctionNode);
	
	/***
	 * 根据权限组来删除用户权限
	 * @param account
	 * @param roleFunctionGroup
	 */
	void deleteRoleFunctionByGroup(Account account,RoleFunctionGroup roleFunctionGroup);

	/***
	 * 获取到取消roleFunctionGroup时，zTree中需要减少的点
	 * 
	 * @param groupId
	 * @param allSelectedGroup
	 * @param allSelectedId
	 * @return
	 */
	JSONObject getZTreeDecreaseData(Long groupId, String[] allSelectedGroups, String[] allSelectedIds);

}
