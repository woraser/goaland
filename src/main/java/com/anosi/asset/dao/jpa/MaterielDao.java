package com.anosi.asset.dao.jpa;

import javax.persistence.EntityManager;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.query.dsl.MustJunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.dao.hibernateSearch.SupplyQuery;
import com.anosi.asset.model.jpa.Materiel;

public interface MaterielDao extends BaseJPADao<Materiel>{

	default public Page<Materiel> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable, String deviceSN) {
		SupplyQuery supplyQuery = (queryBuilder) -> {
			MustJunction mustJunction = queryBuilder.bool()
					.must(queryBuilder.keyword()
							.onFields("name","number")
							.matching(searchContent).createQuery())
					.must(new TermQuery(new Term("device.serialNo", deviceSN)));
			return mustJunction.createQuery();
		};
		return findBySearchContent(entityManager, searchContent, pageable, Materiel.class, supplyQuery, "");
	}
	
}
