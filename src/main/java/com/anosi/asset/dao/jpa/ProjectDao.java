package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Project;

public interface ProjectDao extends BaseJPADao<Project>{

	public Project findByNumber(String number);
	
}
