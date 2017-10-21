package com.anosi.asset.service;

import com.anosi.asset.model.jpa.DepGroup;

public interface DepGroupService extends BaseJPAService<DepGroup>{

	public DepGroup findByCode(String code);
	
}
