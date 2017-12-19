package com.anosi.asset.service.impl;

import com.anosi.asset.service.EntrustDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.EntrustDetailDao;
import com.anosi.asset.model.jpa.EntrustDetail;

@Service("entrustDetailService")
@Transactional
public class EntrustDetailServiceImpl extends BaseJPAServiceImpl<EntrustDetail> implements EntrustDetailService {

	@Autowired
	private EntrustDetailDao entrustDetailDao;

	@Override
	public BaseJPADao<EntrustDetail> getRepository() {
		return entrustDetailDao;
	}

}
