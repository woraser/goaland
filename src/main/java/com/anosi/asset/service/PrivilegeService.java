package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Privilege;

public interface PrivilegeService extends BaseJPAService<Privilege>{

	public Privilege findByRoleFunctionPageId(String roleFunctionPageId);
	
	public Privilege findByAccountAndRoleFunction(String loginId,String roleFunctionPageId);
	
	public void deleteByAccountLoginId(String accountLoginId);
	
}
