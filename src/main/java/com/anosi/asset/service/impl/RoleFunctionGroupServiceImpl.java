package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RoleFunctionGroupDao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionBtn;
import com.anosi.asset.model.jpa.RoleFunctionGroup;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.PrivilegeService;
import com.anosi.asset.service.RoleFunctionBtnService;
import com.anosi.asset.service.RoleFunctionGroupService;
import com.anosi.asset.service.RoleFunctionService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

@Service("roleFunctionGroupService")
@Transactional
public class RoleFunctionGroupServiceImpl extends BaseJPAServiceImpl<RoleFunctionGroup>
		implements RoleFunctionGroupService {

	@Autowired
	private RoleFunctionGroupDao roleFunctionGroupDao;
	@Autowired
	private RoleFunctionService roleFunctionService;
	@Autowired
	private RoleFunctionBtnService roleFunctionBtnService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private PrivilegeService privilegeService;

	@Override
	public BaseJPADao<RoleFunctionGroup> getRepository() {
		return roleFunctionGroupDao;
	}

	@Override
	public JSONArray parseRoleFunctionToTree(Long id) {
		List<RoleFunction> roleFunctions = new ArrayList<>();
		List<RoleFunctionBtn> roleFunctionBtns = new ArrayList<>();
		if (id != null) {
			RoleFunctionGroup roleFunctionGroup = roleFunctionGroupDao.getOne(id);
			// 获得已经勾选的权限
			roleFunctions = roleFunctionGroup.getRoleFunctionList();
			roleFunctionBtns = roleFunctionGroup.getRoleFunctionBtnList();
		}
		return roleFunctionService.parseToTree(roleFunctions, roleFunctionBtns);
	}

	@Override
	public RoleFunctionGroup save(RoleFunctionGroup roleFunctionGroup, String[] selRolesFunctionNode) {
		// 如果是更新，需要更新用户的权限
		if (roleFunctionGroup.getId() != null) {
			if (selRolesFunctionNode != null && selRolesFunctionNode.length != 0) {
				List<Account> accountList = roleFunctionGroup.getAccountList();
				// 先清除用户的相关权限
				accountList.forEach(account -> deleteRoleFunctionByGroup(account, roleFunctionGroup));
				// 清除权限组的权限
				roleFunctionGroup.getRoleFunctionList().clear();
				roleFunctionGroup.getRoleFunctionBtnList().clear();
				// 然后更新权限组的权限
				setRoleFunction(roleFunctionGroup, selRolesFunctionNode);
				// 最后更新用户权限
				accountList.forEach(account -> accountService.resolveRoleFunction(account, selRolesFunctionNode));
			}
		} else {
			save(roleFunctionGroup);
			setRoleFunction(roleFunctionGroup, selRolesFunctionNode);
		}

		return roleFunctionGroup;
	}

	private void setRoleFunction(RoleFunctionGroup roleFunctionGroup, String[] selRolesFunctionNode) {
		if (selRolesFunctionNode != null && selRolesFunctionNode.length != 0) {
			for (String node : selRolesFunctionNode) {
				if (node.contains("menu_")) {
					roleFunctionGroup.getRoleFunctionList()
							.add(roleFunctionService.getOne(Long.parseLong(node.replace("menu_", ""))));
				} else {
					roleFunctionGroup.getRoleFunctionBtnList().add(roleFunctionBtnService.getOne(Long.parseLong(node)));
				}
			}
		}
	}

	@Override
	public void deleteRoleFunctionByGroup(Account account, RoleFunctionGroup roleFunctionGroup) {
		// 清除account拥有的所有权限组中的权限
		roleFunctionGroup.getRoleFunctionList().forEach(roleFunction -> privilegeService.delete(privilegeService
				.findByAccountAndRoleFunction(account.getLoginId(), roleFunction.getRoleFunctionPageId())));
	}

	@Override
	public JSONObject getZTreeDecreaseData(Long groupId, String[] allSelectedGroups, String[] allSelectedIds) {
		RoleFunctionGroup group = getOne(groupId);

		Set<String> groupIdSet = new HashSet<>();// 存放点击的group的权限id
		group.getRoleFunctionList().forEach(roleFunction -> groupIdSet.add("menu_" + roleFunction.getId()));
		group.getRoleFunctionBtnList().forEach(btn -> groupIdSet.add(String.valueOf(btn.getId())));

		Set<String> compareIdSet = new HashSet<>();// 存放所有被选中的group的权限id
		if (allSelectedGroups != null && allSelectedGroups.length != 0) {
			// 所有被选中的group
			for (String selectGroups : allSelectedGroups) {
				if(StringUtils.isNoneBlank(selectGroups)){
					RoleFunctionGroup toCompareGroup = this.roleFunctionGroupDao.getOne(Long.parseLong(selectGroups));
					toCompareGroup.getRoleFunctionList()
							.forEach(roleFunction -> compareIdSet.add("menu_" + roleFunction.getId()));
					toCompareGroup.getRoleFunctionBtnList().forEach(btn -> compareIdSet.add(String.valueOf(btn.getId())));
				}
			}
		}
		// 取差集
		Set<String> differenceWithPid = Sets.difference(groupIdSet, compareIdSet);

		/*
		 * 然后需要判断父权限下是否还有子权限被勾选着,如果有，那么父权限也需要被勾选 如果没有,那么父权限也取消勾选
		 */
		Set<String> checkedPid = getCheckedPid(groupIdSet, allSelectedIds);// 不该被取消的权限id
		Set<String> difference = Sets.difference(differenceWithPid, checkedPid);

		return new JSONObject(ImmutableMap.of("result", difference));
	}

	/***
	 * 找出不该被取消的父权限
	 * 
	 * @param groupIdSet
	 * @param allSelectedIds
	 * @return
	 */
	private Set<String> getCheckedPid(Set<String> groupIdSet, String[] allSelectedIds) {
		Set<String> selectIds = new HashSet<>();
		for (String selectId : allSelectedIds) {
			selectIds.add(selectId);
		}
		// 取出所有勾选权限和权限组权限的差集，然后把所有差集和其父权限返回即可
		Set<String> difference = Sets.difference(selectIds, groupIdSet);
		Set<String> pidResult = new HashSet<>();
		for (String diff : difference) {
			getPid(diff, pidResult);
		}
		return pidResult;
	}

	private void getPid(String diff, Set<String> pidResult) {
		pidResult.add(diff);
		if ("menu_0".equals(diff)) {
			return;
		}
		String pid = "";
		if (diff.contains("menu_")) {
			RoleFunction roleFunction = roleFunctionService.getOne(Long.parseLong(diff.replace("menu_", "")));
			if (roleFunction != null && roleFunction.getParentRoleFunction() != null) {
				pid = "menu_" + roleFunction.getParentRoleFunction().getId();
			}
		} else {
			RoleFunctionBtn roleFunctionBtn = roleFunctionBtnService.getOne(Long.parseLong(diff));
			if (roleFunctionBtn != null && roleFunctionBtn.getRoleFunction() != null) {
				pid = "menu_" + roleFunctionBtn.getRoleFunction().getId();
			}
		}
		if (StringUtils.isNoneBlank(pid)) {
			getPid(pid, pidResult);
		}
	}

}
