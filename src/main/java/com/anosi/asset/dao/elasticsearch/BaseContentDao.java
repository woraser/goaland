package com.anosi.asset.dao.elasticsearch;

import java.io.Serializable;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.anosi.asset.model.elasticsearch.BaseContent;

@SuppressWarnings("hiding")
@NoRepositoryBean
public interface BaseContentDao<T extends BaseContent, ID extends Serializable> extends ElasticsearchRepository<T, ID> {

	public Page<T> findByContentContaining(String content, Pageable pageable);

}
