package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.PrivilegeDao;
import com.anosi.asset.model.jpa.Privilege;
import com.anosi.asset.service.PrivilegeService;

@Service("privilegeService")
@Transactional
public class PrivilegeServiceImpl extends BaseJPAServiceImpl<Privilege> implements PrivilegeService{
	
	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Override
	public BaseJPADao<Privilege> getRepository() {
		return privilegeDao;
	}

	@Override
	public Privilege findByRoleFunctionPageId(String roleFunctionPageId) {
		return privilegeDao.findByRoleFunction_RoleFunctionPageIdEquals(roleFunctionPageId);
	}

	@Override
	public Privilege findByAccountAndRoleFunction(String loginId, String roleFunctionPageId) {
		return privilegeDao.findByAccount_loginIdEqualsAndRoleFunction_RoleFunctionPageIdEquals(loginId, roleFunctionPageId);
	}

	@Override
	public void deleteByAccountLoginId(String accountLoginId) {
		Iterable<Privilege> privileges = privilegeDao.findByAccount_loginIdEquals(accountLoginId);
		privilegeDao.delete(privileges);
	}
	
}
