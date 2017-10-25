package com.anosi.asset.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.anosi.asset.model.jpa.DevCategory;

public interface DevCategoryDao extends BaseJPADao<DevCategory> {

	@Query(value = "SELECT c.category_type,count(d.id) FROM dev_category c LEFT JOIN device d on c.id=d.dev_category_id GROUP BY c.id", nativeQuery = true)
	List<Object[]> countByDevCategory();

}
