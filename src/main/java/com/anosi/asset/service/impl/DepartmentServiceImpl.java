package com.anosi.asset.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DepartmentDao;
import com.anosi.asset.model.jpa.Department;
import com.anosi.asset.service.DepartmentService;

@Service("departmentService")
@Transactional
public class DepartmentServiceImpl extends BaseJPAServiceImpl<Department> implements DepartmentService{
	
	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public Department findByCode(String code) {
		return departmentDao.findByCode(code);
	}

	@Override
	public BaseJPADao<Department> getRepository() {
		return departmentDao;
	}

}
