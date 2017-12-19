package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.EvaluatingDetailDao;
import com.anosi.asset.model.jpa.EvaluatingDetail;
import com.anosi.asset.service.EvaluatingDetailService;

@Service("evaluatingDetailService")
@Transactional
public class EvaluatingDetailServiceImpl extends BaseJPAServiceImpl<EvaluatingDetail> implements EvaluatingDetailService{

	@Autowired
	private EvaluatingDetailDao evaluatingDetailDao;
	
	@Override
	public BaseJPADao<EvaluatingDetail> getRepository() {
		return evaluatingDetailDao;
	}

}
