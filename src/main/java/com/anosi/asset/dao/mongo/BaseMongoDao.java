package com.anosi.asset.dao.mongo;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.anosi.asset.model.mongo.AbstractDocument;

@NoRepositoryBean
public interface BaseMongoDao<T extends AbstractDocument>
		extends MongoRepository<T, BigInteger>, QueryDslPredicateExecutor<T> {

}
