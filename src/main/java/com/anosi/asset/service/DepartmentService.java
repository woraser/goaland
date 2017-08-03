package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Department;

public interface DepartmentService extends BaseService<Department, Long>{

	public Department findByCode(String code);
	
}
