package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.DistrictDao;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.service.DistrictService;

@Service("districtService")
@Transactional
public class DistrictServiceImpl implements DistrictService{
	
	@Autowired
	private DistrictDao districtDao;

	@Override
	public District findByNameAndCity(String districtName, String cityName) {
		return this.districtDao.findByNameEqualsAndCity_NameEquals(districtName, cityName);
	}

	@Override
	public long count() {
		return districtDao.count();
	}

	@Override
	public Iterable<District> save(Iterable<District> districts) {
		return districtDao.save(districts);
	}

}
