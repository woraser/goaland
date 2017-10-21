package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Province;

public interface ProvinceService extends BaseJPAService<Province>{

	public Province findByPID(String PID);
	
}
