package com.anosi.asset.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.mongo.BaseMongoDao;
import com.anosi.asset.model.mongo.AbstractDocument;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

@Transactional
public abstract class BaseMongoServiceImpl<T extends AbstractDocument> extends BaseServiceImpl<T, BigInteger>{

	public abstract BaseMongoDao<T> getRepository();
	
	public void delete(T entity) {
		getRepository().delete(entity);
	}

	public void delete(Iterable<? extends T> entities) {
		getRepository().delete(entities);
	}

	public <S extends T> List<S> save(Iterable<S> entites) {
		return getRepository().save(entites);
	}

	public List<T> findAll() {
		return getRepository().findAll();
	}

	public List<T> findAll(Sort sort) {
		return getRepository().findAll(sort);
	}

	public <S extends T> S insert(S entity) {
		return getRepository().insert(entity);
	}

	public <S extends T> List<S> insert(Iterable<S> entities) {
		return getRepository().insert(entities);
	}

	public <S extends T> List<S> findAll(Example<S> example) {
		return getRepository().findAll(example);
	}

	
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return getRepository().findAll(example, sort);
	}

	
	public T findOne(Predicate predicate) {
		return getRepository().findOne(predicate);
	}

	
	public Iterable<T> findAll(Predicate predicate) {
		return getRepository().findAll(predicate);
	}

	
	public Iterable<T> findAll(Predicate predicate, Sort sort) {
		return getRepository().findAll(predicate,sort);
	}

	
	public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
		return getRepository().findAll(predicate,orders);
	}

	
	public Iterable<T> findAll(OrderSpecifier<?>... orders) {
		return getRepository().findAll(orders);
	}

	
	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		return getRepository().findAll(predicate, pageable);
	}

	
	public long count(Predicate predicate) {
		return getRepository().count(predicate);
	}

	
	public boolean exists(Predicate predicate) {
		return getRepository().exists(predicate);
	}

}
