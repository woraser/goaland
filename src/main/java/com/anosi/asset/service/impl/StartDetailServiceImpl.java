package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.StartDetailDao;
import com.anosi.asset.model.jpa.StartDetail;
import com.anosi.asset.service.StartDetailService;

@Service("startDetailService")
@Transactional
public class StartDetailServiceImpl extends BaseJPAServiceImpl<StartDetail> implements StartDetailService{
	
	@Autowired
	private StartDetailDao startDetailDao;

	@Override
	public BaseJPADao<StartDetail> getRepository() {
		return startDetailDao;
	}

}
