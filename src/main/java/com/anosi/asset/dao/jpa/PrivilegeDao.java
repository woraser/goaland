package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Privilege;

public interface PrivilegeDao extends BaseJPADao<Privilege>{

	public Privilege findByRoleFunction_RoleFunctionPageIdEquals(String roleFunction_roleFunctionPageId);
	
	public Privilege findByAccount_loginIdEqualsAndRoleFunction_RoleFunctionPageIdEquals(String account_loginId,String roleFunction_roleFunctionPageId);
	
}
