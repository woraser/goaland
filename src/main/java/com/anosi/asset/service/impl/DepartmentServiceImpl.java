package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.DepartmentDao;
import com.anosi.asset.model.jpa.Department;
import com.anosi.asset.service.DepartmentService;

@Service("departmentService")
@Transactional
public class DepartmentServiceImpl implements DepartmentService{
	
	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public Department findByCode(String code) {
		return departmentDao.findByCode(code);
	}

	@Override
	public Department save(Department department) {
		return departmentDao.save(department);
	}

}
