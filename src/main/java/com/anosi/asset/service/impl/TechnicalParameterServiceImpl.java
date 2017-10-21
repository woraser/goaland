package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.TechnicalParameterDao;
import com.anosi.asset.model.jpa.TechnicalParameter;
import com.anosi.asset.service.TechnicalParameterService;

@Service("technicalParameterService")
@Transactional
public class TechnicalParameterServiceImpl extends BaseJPAServiceImpl<TechnicalParameter> implements TechnicalParameterService{

	@Autowired
	private TechnicalParameterDao technicalParameterDao;
	
	@Override
	public BaseJPADao<TechnicalParameter> getRepository() {
		return technicalParameterDao;
	}

}
