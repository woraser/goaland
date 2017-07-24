package com.anosi.asset.service;

import com.anosi.asset.model.jpa.District;

public interface DistrictService {
	
	public long count();

	public District findByNameAndCity(String districtName,String cityName);
	
	public Iterable<District> save(Iterable<District> districts);
	
}
