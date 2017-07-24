package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.RoleFunctionDao;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.service.RoleFunctionService;

@Service("roleFunctionService")
@Transactional
public class RoleFunctionServiceImpl implements RoleFunctionService{
	
	@Autowired
	private RoleFunctionDao roleFunctionDao;

	@Override
	public long count() {
		return roleFunctionDao.count();
	}

	@Override
	public RoleFunction findByRoleFunctionPageId(String roleFunctionPageId) {
		return roleFunctionDao.findByRoleFunctionPageIdEquals(roleFunctionPageId);
	}

	@Override
	public RoleFunction save(RoleFunction roleFunction) {
		return roleFunctionDao.save(roleFunction);
	}

}
