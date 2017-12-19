package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DistributeDetailDao;
import com.anosi.asset.model.jpa.DistributeDetail;
import com.anosi.asset.service.DistributeDetailService;

@Service("distributeDetailService")
@Transactional
public class DistributeDetailServiceImpl extends BaseJPAServiceImpl<DistributeDetail>
		implements DistributeDetailService {

	@Autowired
	private DistributeDetailDao distributeDetailDao;
	
	@Override
	public BaseJPADao<DistributeDetail> getRepository() {
		return distributeDetailDao;
	}

}
