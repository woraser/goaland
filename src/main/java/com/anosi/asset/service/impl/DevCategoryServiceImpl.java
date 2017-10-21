package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DevCategoryDao;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.service.DevCategoryService;

@Service("devCategoryService")
@Transactional
public class DevCategoryServiceImpl extends BaseServiceImpl<DevCategory> implements DevCategoryService{
	
	@Autowired
	private DevCategoryDao devCategoryDao;

	@Override
	public BaseJPADao<DevCategory> getRepository() {
		return devCategoryDao;
	}

}
