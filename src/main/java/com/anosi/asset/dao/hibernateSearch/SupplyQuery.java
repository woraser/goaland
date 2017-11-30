package com.anosi.asset.dao.hibernateSearch;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;

@FunctionalInterface
public interface SupplyQuery {

	public Query supplyQuery(QueryBuilder queryBuilder);
	
}
