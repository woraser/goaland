package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RepairDetailDao;
import com.anosi.asset.model.jpa.RepairDetail;
import com.anosi.asset.service.RepairDetailService;

@Service("repairDetailService")
@Transactional
public class RepairDetailServiceImpl extends BaseJPAServiceImpl<RepairDetail> implements RepairDetailService {

	@Autowired
	private RepairDetailDao repairDetailDao;
	
	@Override
	public BaseJPADao<RepairDetail> getRepository() {
		return repairDetailDao;
	}

}
