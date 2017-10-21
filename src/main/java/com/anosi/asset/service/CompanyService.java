package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Company;

public interface CompanyService extends BaseJPAService<Company>{

	public Company findByName(String name);
	
}
