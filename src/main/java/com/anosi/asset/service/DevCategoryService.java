package com.anosi.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.DevCategory;
import com.querydsl.core.types.Predicate;

public interface DevCategoryService {

	public DevCategory save(DevCategory devCategory);
	
	public Iterable<DevCategory> save(Iterable<DevCategory> entities);
	
	public Page<DevCategory> findAll(Predicate predicate, Pageable pageable);
	
}
