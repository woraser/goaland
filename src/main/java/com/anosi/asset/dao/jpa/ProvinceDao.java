package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Province;

public interface ProvinceDao extends BaseJPADao<Province>{

	public Province findByName(String name);
	
}
