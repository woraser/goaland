package com.anosi.asset.dao.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServiceProcessDao extends BaseJPADao<CustomerServiceProcess> {

	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId);

	default public Page<CustomerServiceProcess> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, CustomerServiceProcess.class, "project.name",
				"project.number", "project.location", "name", "applicant.name");
	}

}
