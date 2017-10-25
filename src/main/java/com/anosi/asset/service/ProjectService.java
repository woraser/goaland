package com.anosi.asset.service;

import java.util.List;

import com.anosi.asset.model.jpa.Project;

public interface ProjectService extends BaseJPAService<Project>{
	
	public Project findByNumber(String number);
	
	public List<Project> findByNumberStartsWith(String number);

}
