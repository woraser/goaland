package com.anosi.asset.service;

import com.anosi.asset.model.jpa.FaultCategory;

public interface FaultCategoryService extends BaseJPAService<FaultCategory>{

	public FaultCategory findByName(String name);
	
}
