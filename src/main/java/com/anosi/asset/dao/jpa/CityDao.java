package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.City;

public interface CityDao extends BaseJPADao<City>{

	public City findByName(String name);
	
}
