package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Department;

public interface DepartmentDao extends BaseJPADao<Department>{

	public Department findByCode(String code);
	
}
