package com.anosi.asset.service;

import com.anosi.asset.model.jpa.City;

public interface CityService extends BaseService<City, Long>{

	public City findByCID(String CID);
	
}
