package com.anosi.asset.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJPADao<T> extends JpaRepository<T, Long>,QueryDslPredicateExecutor<T>{

}
