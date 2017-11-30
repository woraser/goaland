package com.anosi.asset.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.anosi.asset.dao.hibernateSearch.SupplyQuery;
import com.anosi.asset.model.jpa.BaseEntity;

@NoRepositoryBean
public interface BaseJPADao<T extends BaseEntity> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {

	/***
	 * hibernate search 模糊搜索
	 * 
	 * @param entityManager
	 * @param searchContent
	 * @param pageable
	 * @param clazz
	 * @param field
	 * @return
	 */
	default public Page<T> findBySearchContent(EntityManager entityManager, String searchContent, Pageable pageable,
			Class<T> clazz, String... field) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
		Query query = queryBuilder.keyword().onFields(field).matching(searchContent).createQuery();
		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, clazz);
		if (pageable != null) {
			// 分页
			jpaQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize());
			// 排序
			pageable.getSort().forEach(s -> {
				boolean reverse = false;
				if (Direction.DESC.equals(s.getDirection())) {
					reverse = true;
				}
				Sort sort = new Sort(new SortField(s.getProperty(), Type.STRING, reverse));
				jpaQuery.setSort(sort);
			});
		}
		@SuppressWarnings("unchecked")
		List<T> ts = jpaQuery.getResultList();
		return new PageImpl<>(ts, pageable, jpaQuery.getResultSize());
	}
	
	/***
	 * hibernate search 模糊搜索
	 * 
	 * @param entityManager
	 * @param searchContent
	 * @param pageable
	 * @param clazz
	 * @param isAdmin
	 * @param type
	 * @param value
	 * @param field
	 * @return
	 */
	default public Page<T> findBySearchContent(EntityManager entityManager, String searchContent, Pageable pageable,
			Class<T> clazz, SupplyQuery supplyQuery, String... field) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
		Query query = null;
		if (supplyQuery != null) {
			query = supplyQuery.supplyQuery(queryBuilder);
		} else {
			query = queryBuilder.keyword().onFields(field).matching(searchContent).createQuery();
		}
		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, clazz);
		if (pageable != null) {
			// 分页
			jpaQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize());
			// 排序
			pageable.getSort().forEach(s -> {
				boolean reverse = false;
				if (Direction.DESC.equals(s.getDirection())) {
					reverse = true;
				}
				Sort sort = new Sort(new SortField(s.getProperty(), Type.STRING, reverse));
				jpaQuery.setSort(sort);
			});
		}
		@SuppressWarnings("unchecked")
		List<T> ts = jpaQuery.getResultList();
		return new PageImpl<>(ts, pageable, jpaQuery.getResultSize());
	}

}
