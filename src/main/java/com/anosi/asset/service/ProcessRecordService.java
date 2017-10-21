package com.anosi.asset.service;

import com.anosi.asset.model.jpa.ProcessRecord;

public interface ProcessRecordService extends BaseJPAService<ProcessRecord>{

	public ProcessRecord findByTaskIdNotEnd(String taskId);
	
}
