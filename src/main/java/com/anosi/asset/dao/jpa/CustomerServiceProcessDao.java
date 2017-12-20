package com.anosi.asset.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.QCustomerServiceProcess;

public interface CustomerServiceProcessDao
		extends BaseJPADao<CustomerServiceProcess>, QuerydslBinderCustomizer<QCustomerServiceProcess> {

	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId);

	default public Page<CustomerServiceProcess> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, CustomerServiceProcess.class,
				"startDetail.project.name", "startDetail.project.number", "startDetail.project.location", "name",
				"applicant.name");
	}

	@Query(value = "SELECT t.TASK_DEF_KEY_,count(*) FROM act_ru_task t LEFT JOIN act_re_procdef p ON t.PROC_DEF_ID_ = p.ID_ where p.KEY_=?1 GROUP BY t.TASK_DEF_KEY_", nativeQuery = true)
	List<Object[]> countTaskDistribute(String processDefKey);

	@Override
	default public void customize(QuerydslBindings bindings, QCustomerServiceProcess qCustomerServiceProcess) {
		bindings.bind(qCustomerServiceProcess.repairDetail.deviceList.any().id).first((path, value) -> {
			return path.eq(value);
		});
	}

}
