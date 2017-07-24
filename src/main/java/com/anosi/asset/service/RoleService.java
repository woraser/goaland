package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Role;

public interface RoleService {

	public long count();

	public Iterable<Role> findAll();
	
	public Role findByCode(String code);
	
	public Role save(Role role);
}
