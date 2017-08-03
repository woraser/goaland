package com.anosi.asset.service;

import com.anosi.asset.model.jpa.ProcessRecord;

public interface ProcessRecordService {

	public ProcessRecord save(ProcessRecord processRecord);
	
	public ProcessRecord findByTaskIdNotEnd(String taskId);
	
}
