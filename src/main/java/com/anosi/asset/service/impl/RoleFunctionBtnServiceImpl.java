package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.RoleFunctionBtnDao;
import com.anosi.asset.model.jpa.RoleFunctionBtn;
import com.anosi.asset.service.RoleFunctionBtnService;

@Service("roleFunctionBtnService")
@Transactional
public class RoleFunctionBtnServiceImpl implements RoleFunctionBtnService{
	
	@Autowired
	private RoleFunctionBtnDao roleFunctionBtnDao;

	@Override
	public long count() {
		return roleFunctionBtnDao.count();
	}

	@Override
	public RoleFunctionBtn save(RoleFunctionBtn roleFunctionBtn) {
		return roleFunctionBtnDao.save(roleFunctionBtn);
	}

	@Override
	public RoleFunctionBtn findByBtnIdAndRoleFunction(String btnId, String roleFunctionPageId) {
		return roleFunctionBtnDao.findByBtnIdEqualsAndRoleFunction_roleFunctionPageIdEquals(btnId, roleFunctionPageId);
	}

}
