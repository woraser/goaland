package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Role;

public interface RoleDao extends BaseJPADao<Role>{
	
	public Role findByCodeEquals(String code);
	
}
