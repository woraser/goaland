package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.FaultCategory;

public interface FaultCategoryDao extends BaseJPADao<FaultCategory>{

	public FaultCategory findByName(String name);
	
}
