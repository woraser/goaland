package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.RoleFunction;

public interface RoleFunctionDao extends BaseJPADao<RoleFunction>{

	public RoleFunction findByRoleFunctionPageIdEquals(String roleFunctionPageId);
	
}
