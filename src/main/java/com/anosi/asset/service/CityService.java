package com.anosi.asset.service;

import com.anosi.asset.model.jpa.City;

public interface CityService extends BaseJPAService<City>{

	public City findByCID(String CID);
	
}
