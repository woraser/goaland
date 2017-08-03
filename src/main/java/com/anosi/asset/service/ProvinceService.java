package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Province;

public interface ProvinceService extends BaseService<Province, Long>{

	public Province findByPID(String PID);
	
}
