package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.dao.jpa.AccountDao;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.Privilege;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionBtn;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.PrivilegeService;
import com.anosi.asset.service.RoleFunctionBtnService;
import com.anosi.asset.service.RoleFunctionService;

@Service("accountService")
@Transactional
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private RoleFunctionService roleFunctionService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleFunctionBtnService roleFunctionBtnService;

	@Override
	public BaseJPADao<Account> getRepository() {
		return accountDao;
	}

	@Override
	public Account findByLoginId(String loginId) {
		logger.debug("findByLoginId:{}", loginId);
		return this.accountDao.findByLoginId(loginId);
	}

	/***
	 * 复写save方法
	 * 
	 */
	@Override
	public Account save(Account account, String password,String[] selRolesFunctionNode) {
		account.setPassword(password);
		try {
			// 设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(selRolesFunctionNode!=null&&selRolesFunctionNode.length!=0){
			return save(account,selRolesFunctionNode);
		}else{
			return save(account);
		}
	}
	
	@Override
	public Account save(Account account, String[] selRolesFunctionNode) {
		account = save(account);
		privilegeService.deleteByAccountLoginId(account.getLoginId());
		for (String node : selRolesFunctionNode) {
			if(node.contains("menu_")){
				Privilege privilege = new Privilege();
				privilege.setAccount(account);
				privilege.setRoleFunction(roleFunctionService.getOne(Long.parseLong(node.replace("menu_", ""))));
				privilegeService.save(privilege);
			}else{
				RoleFunctionBtn btn = roleFunctionBtnService.getOne(Long.parseLong(node));
				Privilege privilege = privilegeService.findByAccountAndRoleFunction(account.getLoginId(), btn.getRoleFunction().getRoleFunctionPageId());
				if(privilege!=null){
					privilege.getRoleFunctionBtnList().add(btn);
				}else{
					privilege = new Privilege();
					privilege.setAccount(account);
					privilege.setRoleFunction(btn.getRoleFunction());
					privilege.getRoleFunctionBtnList().add(btn);
					privilegeService.save(privilege);
				}
			}
		}
		return account;
	}

	@Override
	public JSONArray parseRoleFunctionToTree(Long id) {
		List<RoleFunction> roleFunctions = new ArrayList<>();
		List<RoleFunctionBtn> roleFunctionBtns = new ArrayList<>();
		if (id != null) {
			Account account = accountDao.getOne(id);
			List<Privilege> privilegeList = account.getPrivilegeList();
			// 获取用户已经有的权限
			roleFunctions = privilegeList.stream().map(Privilege::getRoleFunction).collect(Collectors.toList());
			roleFunctionBtns = privilegeList.stream().map(Privilege::getRoleFunctionBtnList).collect(
					() -> new ArrayList<RoleFunctionBtn>(), (list, item) -> list.addAll(item),
					(list1, list2) -> list1.addAll(list2));
		} 
		return roleFunctionService.parseToTree(roleFunctions, roleFunctionBtns);
	}

	@Override
	public Iterable<Account> findByIsUploadDocument(boolean isUploadDocument) {
		return accountDao.findByUploadDocument(isUploadDocument);
	}

}
