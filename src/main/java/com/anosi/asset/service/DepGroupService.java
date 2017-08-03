package com.anosi.asset.service;

import com.anosi.asset.model.jpa.DepGroup;

public interface DepGroupService extends BaseService<DepGroup, Long>{

	public DepGroup findByCode(String code);
	
}
