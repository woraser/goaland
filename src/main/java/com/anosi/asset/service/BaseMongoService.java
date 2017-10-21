package com.anosi.asset.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.anosi.asset.model.mongo.AbstractDocument;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public interface BaseMongoService<T extends AbstractDocument> extends BaseService<T, BigInteger> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
	 */
	@Override
	<S extends T> List<S> save(Iterable<S> entites);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	@Override
	List<T> findAll();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
	@Override
	List<T> findAll(Sort sort);

	/**
	 * Inserts the given entity. Assumes the instance to be new to be able to apply insertion optimizations. Use
	 * the returned instance for further operations as the save operation might have changed the entity instance
	 * completely. Prefer using {@link #save(Object)} instead to avoid the usage of store-specific API.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity
	 * @since 1.7
	 */
	<S extends T> S insert(S entity);

	/**
	 * Inserts the given entities. Assumes the given entities to have not been persisted yet and thus will optimize the
	 * insert over a call to {@link #save(Iterable)}. Prefer using {@link #save(Iterable)} to avoid the usage of store
	 * specific API.
	 *
	 * @param entities must not be {@literal null}.
	 * @return the saved entities
	 * @since 1.7
	 */
	<S extends T> List<S> insert(Iterable<S> entities);

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example)
	 */
	<S extends T> List<S> findAll(Example<S> example);

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
	 */
	<S extends T> List<S> findAll(Example<S> example, Sort sort);
	
	/**
	 * Returns a single entity matching the given {@link Predicate} or {@literal null} if none was found.
	 * 
	 * @param predicate can be {@literal null}.
	 * @return a single entity matching the given {@link Predicate} or {@literal null} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the predicate yields more than one
	 *           result.
	 */
	T findOne(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate}. In case no match could be found an empty
	 * {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 */
	Iterable<T> findAll(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link Sort}. In case no match could
	 * be found an empty {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 * @since 1.10
	 */
	Iterable<T> findAll(Predicate predicate, Sort sort);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s. In case no
	 * match could be found an empty {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param orders the {@link OrderSpecifier}s to sort the results by
	 * @return all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s.
	 */
	Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	/**
	 * Returns all entities ordered by the given {@link OrderSpecifier}s.
	 * 
	 * @param orders the {@link OrderSpecifier}s to sort the results by.
	 * @return all entities ordered by the given {@link OrderSpecifier}s.
	 */
	Iterable<T> findAll(OrderSpecifier<?>... orders);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Predicate}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param pageable can be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Predicate}.
	 */
	Page<T> findAll(Predicate predicate, Pageable pageable);

	/**
	 * Returns the number of instances matching the given {@link Predicate}.
	 * 
	 * @param predicate the {@link Predicate} to count instances for, can be {@literal null}.
	 * @return the number of instances matching the {@link Predicate}.
	 */
	long count(Predicate predicate);

	/**
	 * Checks whether the data store contains elements that match the given {@link Predicate}.
	 *
	 * @param predicate the {@link Predicate} to use for the existance check, can be {@literal null}.
	 * @return {@literal true} if the data store contains elements that match the given {@link Predicate}.
	 */
	boolean exists(Predicate predicate);
	
}
