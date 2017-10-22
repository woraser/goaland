package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CountryDao;
import com.anosi.asset.model.jpa.Country;
import com.anosi.asset.service.CountryService;

@Transactional
@Service("countryService")
public class CountryServiceImpl extends BaseJPAServiceImpl<Country> implements CountryService{
	
	@Autowired
	private CountryDao countryDao;

	@Override
	public BaseJPADao<Country> getRepository() {
		return countryDao;
	}

	@Override
	public Country findByName(String name) {
		return countryDao.findByName(name);
	}

}
