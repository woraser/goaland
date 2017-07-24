package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Company;

public interface CompanyDao extends BaseJPADao<Company>{

	public Company findByNameEquals(String name);
	
}
