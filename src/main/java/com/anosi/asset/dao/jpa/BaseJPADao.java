package com.anosi.asset.dao.jpa;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface BaseJPADao<T> extends CrudRepository<T, Long>,QueryDslPredicateExecutor<T>{

}
