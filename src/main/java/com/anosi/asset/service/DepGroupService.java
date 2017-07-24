package com.anosi.asset.service;

import com.anosi.asset.model.jpa.DepGroup;

public interface DepGroupService {

	public DepGroup findByCode(String code);
	
	public DepGroup save(DepGroup depGroup);
	
}
