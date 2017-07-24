package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.RoleFunctionBtn;

public interface RoleFunctionBtnDao extends BaseJPADao<RoleFunctionBtn>{
	
	public RoleFunctionBtn findByBtnIdEqualsAndRoleFunction_roleFunctionPageIdEquals(String btnId,String roleFunctionPageId);
	
}
