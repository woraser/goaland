package com.anosi.asset.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseJPADao<T> extends JpaRepository<T, Long>,QueryDslPredicateExecutor<T>{

}
