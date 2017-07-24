package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Privilege;

public interface PrivilegeService {

	public Privilege findByRoleFunctionPageId(String roleFunctionPageId);
	
	public Privilege save(Privilege privilege);
	
	public Privilege findByAccountAndRoleFunction(String loginId,String roleFunctionPageId);
	
}
