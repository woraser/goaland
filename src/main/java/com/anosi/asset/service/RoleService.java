package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Role;

public interface RoleService extends BaseJPAService<Role>{

	public Role findByCode(String code);
	
}
