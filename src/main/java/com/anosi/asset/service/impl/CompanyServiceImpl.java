package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.CompanyDao;
import com.anosi.asset.model.jpa.Company;
import com.anosi.asset.service.CompanyService;

@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	private CompanyDao companyDao;

	@Override
	public Iterable<Company> findAll() {
		return companyDao.findAll();
	}

	@Override
	public Company save(Company company) {
		return companyDao.save(company);
	}

	@Override
	public Company findByName(String name) {
		return companyDao.findByNameEquals(name);
	}

}
