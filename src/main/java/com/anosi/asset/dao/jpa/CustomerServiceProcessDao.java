package com.anosi.asset.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServiceProcessDao extends BaseJPADao<CustomerServiceProcess> {

	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId);

	default public Page<CustomerServiceProcess> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, CustomerServiceProcess.class, "project.name",
				"project.number", "project.location", "name", "applicant.name");
	}

	@Query(value = "SELECT t.TASK_DEF_KEY_,count(*) FROM act_ru_task t LEFT JOIN act_re_procdef p ON t.PROC_DEF_ID_ = p.ID_ where p.KEY_=?1 GROUP BY t.TASK_DEF_KEY_", nativeQuery = true)
	List<Object[]> countTaskDistribute(String processDefKey);

	public long countByDevice_devCategory_idEqualsAndProcessInstanceIdIn(Long devCateogryId,
			List<String> processInstanceIds);
	
	public List<CustomerServiceProcess> findByDevice_devCategory_idEqualsAndProcessInstanceIdIn(Long devCateogryId,
			List<String> processInstanceIds);

}
