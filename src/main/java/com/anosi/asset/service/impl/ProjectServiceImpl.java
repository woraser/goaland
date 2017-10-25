package com.anosi.asset.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ProjectDao;
import com.anosi.asset.model.jpa.Project;
import com.anosi.asset.service.ProjectService;

@Service("projectService")
@Transactional
public class ProjectServiceImpl extends BaseJPAServiceImpl<Project> implements ProjectService{
	
	@Autowired
	private ProjectDao projectDao;

	@Override
	public BaseJPADao<Project> getRepository() {
		return projectDao;
	}

	@Override
	public Project findByNumber(String number) {
		return projectDao.findByNumber(number);
	}

	@Override
	public List<Project> findByNumberStartsWith(String number) {
		return projectDao.findByNumberStartsWith(number);
	}

}
