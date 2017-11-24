package com.anosi.asset.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.AdvertisementDao;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.model.jpa.Advertisement;
import com.anosi.asset.service.AdvertisementService;

@Transactional
@Service("advertisementService")
public class AdvertisementServiceImpl extends BaseJPAServiceImpl<Advertisement> implements AdvertisementService {

	@Autowired
	private AdvertisementDao advertisementDao;
	@Autowired
	private EntityManager entityManager;

	@Override
	public BaseJPADao<Advertisement> getRepository() {
		return advertisementDao;
	}

	@Override
	public Page<Advertisement> findByContentSearch(String searchContent, Pageable pageable) {
		return advertisementDao.findBySearchContent(entityManager, searchContent, pageable);
	}

}
