package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Project;

public interface ProjectService extends BaseJPAService<Project>{
	
	public Project findByNumber(String number);

}
