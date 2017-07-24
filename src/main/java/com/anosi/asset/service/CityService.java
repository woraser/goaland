package com.anosi.asset.service;

import com.anosi.asset.model.jpa.City;

public interface CityService {

	public long count();
	
	public City findByCID(String CID);
	
	public Iterable<City> save(Iterable<City> citys);
	
}
