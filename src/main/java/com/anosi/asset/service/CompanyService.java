package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Company;

public interface CompanyService {

	public Iterable<Company> findAll();
	
	public Company save(Company company);
	
	public Company findByName(String name);
	
}
