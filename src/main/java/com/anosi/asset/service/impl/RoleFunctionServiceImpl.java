package com.anosi.asset.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RoleFunctionDao;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.service.RoleFunctionService;

@Service("roleFunctionService")
@Transactional
public class RoleFunctionServiceImpl extends BaseServiceImpl<RoleFunction> implements RoleFunctionService{
	
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

}
