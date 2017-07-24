package com.anosi.asset.service;

import com.anosi.asset.model.jpa.RoleFunction;

public interface RoleFunctionService {

	public long count();
	
	public RoleFunction findByRoleFunctionPageId(String roleFunctionPageId);
	
	public RoleFunction save(RoleFunction roleFunction);
	
}
