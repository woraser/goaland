package com.anosi.asset.dao.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Advertisement;

public interface AdvertisementDao extends BaseJPADao<Advertisement>{

	default public Page<Advertisement> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, Advertisement.class, "name",
				"creater.name", "content");
	}
	
}
