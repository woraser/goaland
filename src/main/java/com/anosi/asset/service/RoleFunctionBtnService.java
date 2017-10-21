package com.anosi.asset.service;

import com.anosi.asset.model.jpa.RoleFunctionBtn;

public interface RoleFunctionBtnService extends BaseJPAService<RoleFunctionBtn>{

	public RoleFunctionBtn findByBtnIdAndRoleFunction(String btnId,String roleFunctionPageId);
	
}
