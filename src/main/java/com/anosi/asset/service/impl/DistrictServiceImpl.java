package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DistrictDao;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.service.DistrictService;

@Service("districtService")
@Transactional
public class DistrictServiceImpl extends BaseJPAServiceImpl<District> implements DistrictService{
	
	@Autowired
	private DistrictDao districtDao;

	@Override
	public District findByNameAndCity(String districtName, String cityName) {
		return this.districtDao.findByNameEqualsAndCity_NameEquals(districtName, cityName);
	}

	@Override
	public BaseJPADao<District> getRepository() {
		return districtDao;
	}

}
