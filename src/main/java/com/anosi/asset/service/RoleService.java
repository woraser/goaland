package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Role;

public interface RoleService extends BaseService<Role, Long>{

	public Role findByCode(String code);
	
}
