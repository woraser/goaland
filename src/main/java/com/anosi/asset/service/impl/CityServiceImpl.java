package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CityDao;
import com.anosi.asset.model.jpa.City;
import com.anosi.asset.service.CityService;

@Service("cityService")
@Transactional
public class CityServiceImpl extends BaseJPAServiceImpl<City> implements CityService{

	@Autowired
	private CityDao cityDao;
	
	@Override
	public BaseJPADao<City> getRepository() {
		return cityDao;
	}

	@Override
	public City findByName(String name) {
		return cityDao.findByName(name);
	}

}
