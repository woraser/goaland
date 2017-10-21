package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ProcessRecordDao;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.service.ProcessRecordService;

@Service("processRecordService")
@Transactional
public class ProcessRecordServiceImpl extends BaseJPAServiceImpl<ProcessRecord> implements ProcessRecordService {
	
	@Autowired
	private ProcessRecordDao processRecordDao;

	@Override
	public ProcessRecord findByTaskIdNotEnd(String taskId) {
		return processRecordDao.findByTaskIdEqualsAndEndTimeIsNull(taskId);
	}

	@Override
	public BaseJPADao<ProcessRecord> getRepository() {
		return processRecordDao;
	}
	
}
