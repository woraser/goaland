package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.DepGroupDao;
import com.anosi.asset.model.jpa.DepGroup;
import com.anosi.asset.service.DepGroupService;

@Service("depGroupService")
@Transactional
public class DepGroupServiceImpl implements DepGroupService{
	
	@Autowired
	private DepGroupDao depGroupDao;

	@Override
	public DepGroup findByCode(String code) {
		return depGroupDao.findByCode(code);
	}

	@Override
	public DepGroup save(DepGroup depGroup) {
		return depGroupDao.save(depGroup);
	}

}
