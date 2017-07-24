package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.ProvinceDao;
import com.anosi.asset.model.jpa.Province;
import com.anosi.asset.service.ProvinceService;

@Service("provinceService")
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public long count() {
		return provinceDao.count();
	}

	@Override
	public Province findByPID(String PID) {
		return provinceDao.findByPid(PID);
	}

	@Override
	public Iterable<Province> save(Iterable<Province> provinces) {
		return provinceDao.save(provinces);
	}
	
}
