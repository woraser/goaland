package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.DepGroup;

public interface DepGroupDao extends BaseJPADao<DepGroup> {
	
	public DepGroup findByCode(String code);
	
}
