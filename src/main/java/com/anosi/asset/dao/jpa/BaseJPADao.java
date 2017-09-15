package com.anosi.asset.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.anosi.asset.model.jpa.BaseEntity;

@NoRepositoryBean
public interface BaseJPADao<T extends BaseEntity> extends JpaRepository<T, Long>,QueryDslPredicateExecutor<T>{

}
