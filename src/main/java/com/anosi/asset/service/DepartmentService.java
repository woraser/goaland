package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Department;

public interface DepartmentService {

	public Department findByCode(String code);
	
	public Department save(Department department);
	
}
