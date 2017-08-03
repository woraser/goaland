package com.anosi.asset.service.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.anosi.asset.model.jpa.BaseEntity;
import com.anosi.asset.service.BaseService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T,Long>{

	@Override
	public <S extends T> S save(S entity) {
		return getRepository().save(entity);
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return getRepository().save(entities);
	}

	@Override
	public T findOne(Long id) {
		return getRepository().findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return getRepository().exists(id);
	}

	@Override
	public Iterable<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public Iterable<T> findAll(Iterable<Long> ids) {
		return getRepository().findAll(ids);
	}

	@Override
	public long count() {
		return getRepository().count();
	}

	@Override
	public void delete(Long id) {
		getRepository().delete(id);
	}

	@Override
	public void delete(T entity) {
		getRepository().delete(entity);
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		getRepository().delete(entities);
	}

	@Override
	public void deleteAll() {
		getRepository().deleteAll();
	}

	@Override
	public Iterable<T> findAll(Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return getRepository().findAll(pageable);
	}

	@Override
	public void flush() {
		getRepository().flush();
	}

	@Override
	public <S extends T> S saveAndFlush(S entity) {
		return getRepository().saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<T> entities) {
		getRepository().deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch() {
		getRepository().deleteAllInBatch();
	}

	@Override
	public T getOne(Long id) {
		return getRepository().getOne(id);
	}

	@Override
	public T findOne(Predicate predicate) {
		return getRepository().findOne(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate) {
		return getRepository().findAll(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, Sort sort) {
		return getRepository().findAll(predicate, sort);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
		return getRepository().findAll(predicate,orders);
	}

	@Override
	public Iterable<T> findAll(OrderSpecifier<?>... orders) {
		return getRepository().findAll(orders);
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		return getRepository().findAll(predicate, pageable);
	}

	@Override
	public long count(Predicate predicate) {
		return getRepository().count(predicate);
	}

	@Override
	public boolean exists(Predicate predicate) {
		return getRepository().exists(predicate);
	}

	
	
}
