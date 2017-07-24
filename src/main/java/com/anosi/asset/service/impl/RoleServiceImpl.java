package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.RoleDao;
import com.anosi.asset.model.jpa.Role;
import com.anosi.asset.service.RoleService;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Override
	public long count() {
		return roleDao.count();
	}
	
	@Override
	public Iterable<Role> findAll(){
		return roleDao.findAll();
	}

	@Override
	public Role findByCode(String code) {
		return roleDao.findByCodeEquals(code);
	}

	@Override
	public Role save(Role role) {
		return roleDao.save(role);
	}
	
}
