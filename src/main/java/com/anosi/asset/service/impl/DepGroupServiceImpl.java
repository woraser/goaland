package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DepGroupDao;
import com.anosi.asset.model.jpa.DepGroup;
import com.anosi.asset.service.DepGroupService;

@Service("depGroupService")
@Transactional
public class DepGroupServiceImpl extends BaseServiceImpl<DepGroup> implements DepGroupService{
	
	@Autowired
	private DepGroupDao depGroupDao;

	@Override
	public DepGroup findByCode(String code) {
		return depGroupDao.findByCode(code);
	}

	@Override
	public BaseJPADao<DepGroup> getRepository() {
		return depGroupDao;
	}

}
