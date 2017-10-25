package com.anosi.asset.dao.jpa;

import java.util.List;

import com.anosi.asset.model.jpa.Project;

public interface ProjectDao extends BaseJPADao<Project>{

	public Project findByNumber(String number);
	
	public List<Project> findByNumberStartsWith(String number);
	
}
