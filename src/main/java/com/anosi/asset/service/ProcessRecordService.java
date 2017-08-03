package com.anosi.asset.service;

import com.anosi.asset.model.jpa.ProcessRecord;

public interface ProcessRecordService extends BaseService<ProcessRecord, Long>{

	public ProcessRecord findByTaskIdNotEnd(String taskId);
	
}
