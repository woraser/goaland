package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CompanyDao;
import com.anosi.asset.model.jpa.Company;
import com.anosi.asset.service.CompanyService;

@Service("companyService")
@Transactional
public class CompanyServiceImpl extends BaseJPAServiceImpl<Company> implements CompanyService{
	
	@Autowired
	private CompanyDao companyDao;

	@Override
	public BaseJPADao<Company> getRepository() {
		return companyDao;
	}

	@Override
	public Company findByName(String name) {
		return companyDao.findByNameEquals(name);
	}

}
