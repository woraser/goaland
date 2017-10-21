package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Department;

public interface DepartmentService extends BaseJPAService<Department>{

	public Department findByCode(String code);
	
}
