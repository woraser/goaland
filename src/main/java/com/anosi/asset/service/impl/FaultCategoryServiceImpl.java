package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.FaultCategoryDao;
import com.anosi.asset.model.jpa.FaultCategory;
import com.anosi.asset.service.FaultCategoryService;

@Service("faultCategoryService")
@Transactional
public class FaultCategoryServiceImpl extends BaseJPAServiceImpl<FaultCategory> implements FaultCategoryService{

	@Autowired
	private FaultCategoryDao faultCategoryDao;
	
	@Override
	public BaseJPADao<FaultCategory> getRepository() {
		return faultCategoryDao;
	}
	
	@Override
	public FaultCategory findByName(String name){
		return faultCategoryDao.findByName(name);
	}

}
