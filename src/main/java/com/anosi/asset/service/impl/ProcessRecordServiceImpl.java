package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ProcessRecordDao;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.service.ProcessRecordService;

@Service("processRecordService")
@Transactional
public class ProcessRecordServiceImpl extends BaseServiceImpl<ProcessRecord> implements ProcessRecordService {
	
	@Autowired
	private ProcessRecordDao processRecordDao;

	@Override
	public ProcessRecord findByTaskIdNotEnd(String taskId) {
		return processRecordDao.findByTaskIdEqualsAndEndTimeIsNotNull(taskId);
	}

	@Override
	public BaseJPADao<ProcessRecord> getRepository() {
		return processRecordDao;
	}
	
}
