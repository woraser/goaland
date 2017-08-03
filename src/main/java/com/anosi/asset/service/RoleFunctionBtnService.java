package com.anosi.asset.service;

import com.anosi.asset.model.jpa.RoleFunctionBtn;

public interface RoleFunctionBtnService extends BaseService<RoleFunctionBtn, Long>{

	public RoleFunctionBtn findByBtnIdAndRoleFunction(String btnId,String roleFunctionPageId);
	
}
