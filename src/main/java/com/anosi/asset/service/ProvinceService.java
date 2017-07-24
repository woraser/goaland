package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Province;

public interface ProvinceService {

	public long count();
	
	public Province findByPID(String PID);
	
	public Iterable<Province> save(Iterable<Province> provinces);
	
}
