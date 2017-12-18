package com.anosi.asset.dao.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Account;

public interface AccountDao extends BaseJPADao<Account> {

	public Account findByLoginId(String loginId);

	public Iterable<Account> findByUploadDocument(boolean uploadDocument);
	
	default public Page<Account> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, Account.class, "name", "loginId",
				"roleList.name", "roleList.depGroup.name", "roleList.depGroup.department.name");
	}

}
