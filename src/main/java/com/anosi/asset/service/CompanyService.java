package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Company;

public interface CompanyService extends BaseService<Company,Long>{

	public Company findByName(String name);
	
}
