package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.CityDao;
import com.anosi.asset.model.jpa.City;
import com.anosi.asset.service.CityService;

@Service("cityService")
@Transactional
public class CityServiceImpl implements CityService{

	@Autowired
	private CityDao cityDao;

	@Override
	public long count() {
		return cityDao.count();
	}

	@Override
	public City findByCID(String CID) {
		return cityDao.findByCid(CID);
	}

	@Override
	public Iterable<City> save(Iterable<City> citys) {
		return cityDao.save(citys);
	}
	
}
