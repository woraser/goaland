package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RoleDao;
import com.anosi.asset.model.jpa.Role;
import com.anosi.asset.service.RoleService;

@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseJPAServiceImpl<Role> implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Override
	public Role findByCode(String code) {
		return roleDao.findByCodeEquals(code);
	}

	@Override
	public BaseJPADao<Role> getRepository() {
		return roleDao;
	}
	
}
