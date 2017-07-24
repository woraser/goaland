package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServiceProcessDao extends BaseJPADao<CustomerServiceProcess>{

	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId);
	
}
