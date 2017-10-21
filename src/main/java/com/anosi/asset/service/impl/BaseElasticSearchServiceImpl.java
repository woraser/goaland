package com.anosi.asset.service.impl;

import java.io.Serializable;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.anosi.asset.dao.elasticsearch.BaseElasticSearchDao;
import com.anosi.asset.model.elasticsearch.BaseElasticSearchModel;

public abstract class BaseElasticSearchServiceImpl<T extends BaseElasticSearchModel, ID extends Serializable> extends BaseServiceImpl<T, ID> {

	public abstract BaseElasticSearchDao<T, ID> getRepository();
	
	public <S extends T> S index(S entity){
		return getRepository().index(entity);
	}

	public Iterable<T> search(QueryBuilder query) {
		return getRepository().search(query);
	}

	public Page<T> search(QueryBuilder query, Pageable pageable) {
		return getRepository().search(query, pageable);
	}

	public Page<T> search(SearchQuery searchQuery) {
		return getRepository().search(searchQuery);
	}

	public Page<T> searchSimilar(T entity, String[] fields, Pageable pageable) {
		return getRepository().searchSimilar(entity, fields, pageable);
	}

	public void refresh() {
		getRepository().refresh();
	}

	public Class<T> getEntityClass() {
		return getRepository().getEntityClass();
	}
	
}
