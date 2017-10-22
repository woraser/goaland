package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Country;

public interface CountryDao extends BaseJPADao<Country>{

	public Country findByName(String name);
	
}
