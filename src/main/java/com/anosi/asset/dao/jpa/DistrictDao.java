package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.District;

public interface DistrictDao extends BaseJPADao<District>{

	public District findByNameEqualsAndCity_NameEquals(String name,String cityName);
	
}
