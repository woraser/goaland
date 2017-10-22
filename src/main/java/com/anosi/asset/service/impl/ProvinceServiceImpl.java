package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ProvinceDao;
import com.anosi.asset.model.jpa.Province;
import com.anosi.asset.service.ProvinceService;

@Service("provinceService")
@Transactional
public class ProvinceServiceImpl extends BaseJPAServiceImpl<Province> implements ProvinceService{

	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public Province findByName(String name) {
		return provinceDao.findByName(name);
	}

	@Override
	public BaseJPADao<Province> getRepository() {
		return provinceDao;
	}
	
}
