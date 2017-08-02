package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.DevCategoryDao;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.service.DevCategoryService;
import com.querydsl.core.types.Predicate;

@Service("devCategoryService")
@Transactional
public class DevCategoryServiceImpl implements DevCategoryService{
	
	@Autowired
	private DevCategoryDao devCategoryDao;

	@Override
	public DevCategory save(DevCategory devCategory) {
		return devCategoryDao.save(devCategory);
	}

	@Override
	public Page<DevCategory> findAll(Predicate predicate, Pageable pageable) {
		return devCategoryDao.findAll(predicate, pageable);
	}

	@Override
	public Iterable<DevCategory> save(Iterable<DevCategory> entities) {
		return devCategoryDao.save(entities);
	}

}
