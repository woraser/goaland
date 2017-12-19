package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ExamineDetailDao;
import com.anosi.asset.model.jpa.ExamineDetail;
import com.anosi.asset.service.ExamineDetailService;

@Service("examineDetailService")
@Transactional
public class ExamineDetailServiceImpl extends BaseJPAServiceImpl<ExamineDetail> implements ExamineDetailService{

	@Autowired
	private ExamineDetailDao examineDetailDao;
	
	@Override
	public BaseJPADao<ExamineDetail> getRepository() {
		return examineDetailDao;
	}

}
