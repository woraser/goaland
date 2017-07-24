package com.anosi.asset.service;

import com.anosi.asset.model.jpa.RoleFunctionBtn;

public interface RoleFunctionBtnService {

	public long count();
	
	public RoleFunctionBtn save(RoleFunctionBtn roleFunctionBtn);
	
	public RoleFunctionBtn findByBtnIdAndRoleFunction(String btnId,String roleFunctionPageId);
	
}
