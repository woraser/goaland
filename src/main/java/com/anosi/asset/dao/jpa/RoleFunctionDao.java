package com.anosi.asset.dao.jpa;

import java.util.List;

import com.anosi.asset.model.jpa.RoleFunction;

public interface RoleFunctionDao extends BaseJPADao<RoleFunction>{

	public RoleFunction findByRoleFunctionPageIdEquals(String roleFunctionPageId);
	
	public List<RoleFunction> findByParentRoleFunctionIsNull();
	
}
