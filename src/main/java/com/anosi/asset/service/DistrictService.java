package com.anosi.asset.service;

import com.anosi.asset.model.jpa.District;

public interface DistrictService extends BaseJPAService<District>{
	
	public District findByNameAndCity(String districtName,String cityName);
	
}
