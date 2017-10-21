package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Project;

public interface ProjectService extends BaseService<Project, Long>{
	
	public Project findByNumber(String number);

}
