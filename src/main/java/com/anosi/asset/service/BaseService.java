package com.anosi.asset.service;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/***
 * 把JpaRepository和QueryDslPredicateExecutor中的方法都搬了过来
 * @author jinyao
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseService<T,ID extends Serializable> {
	
	public BaseJPADao<T> getRepository();
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> S save(S entity);

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T> Iterable<S> save(Iterable<S> entities);

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	T findOne(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	boolean exists(ID id);

	/**
	 * Returns all instances of the type.
	 * 
	 * @return all entities
	 */
	Iterable<T> findAll();

	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param ids
	 * @return
	 */
	Iterable<T> findAll(Iterable<ID> ids);

	/**
	 * Returns the number of entities available.
	 * 
	 * @return the number of entities
	 */
	long count();

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	void delete(ID id);

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	void delete(T entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	void delete(Iterable<? extends T> entities);

	/**
	 * Deletes all entities managed by the repository.
	 */
	void deleteAll();
	
	/**
	 * Returns all entities sorted by the given options.
	 * 
	 * @param sort
	 * @return all entities sorted by the given options
	 */
	Iterable<T> findAll(Sort sort);

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable);
	
	/**
	 * Flushes all pending changes to the database.
	 */
	void flush();

	/**
	 * Saves an entity and flushes changes instantly.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> S saveAndFlush(S entity);

	/**
	 * Deletes the given entities in a batch which means it will create a single {@link Query}. Assume that we will clear
	 * the {@link javax.persistence.EntityManager} after the call.
	 * 
	 * @param entities
	 */
	void deleteInBatch(Iterable<T> entities);

	/**
	 * Deletes all entities in a batch call.
	 */
	void deleteAllInBatch();

	/**
	 * Returns a reference to the entity with the given identifier.
	 * 
	 * @param id must not be {@literal null}.
	 * @return a reference to the entity with the given identifier.
	 * @see EntityManager#getReference(Class, Object)
	 */
	T getOne(ID id);
	
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
