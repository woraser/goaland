package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Country;

public interface CountryService extends BaseJPAService<Country>{

	Country findByName(String name);
	
}
