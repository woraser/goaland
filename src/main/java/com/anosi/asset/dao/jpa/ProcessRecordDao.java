package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.ProcessRecord;

public interface ProcessRecordDao extends BaseJPADao<ProcessRecord> {

	public ProcessRecord findByTaskIdEqualsAndEndTimeIsNull(String taskId);
	
}
