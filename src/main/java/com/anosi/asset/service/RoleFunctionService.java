package com.anosi.asset.service;

import com.anosi.asset.model.jpa.RoleFunction;

public interface RoleFunctionService extends BaseService<RoleFunction, Long>{

	public RoleFunction findByRoleFunctionPageId(String roleFunctionPageId);
	
}
