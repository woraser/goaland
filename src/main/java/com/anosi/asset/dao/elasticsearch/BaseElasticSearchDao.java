package com.anosi.asset.dao.elasticsearch;

import java.io.Serializable;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.anosi.asset.model.elasticsearch.BaseElasticSearchModel;

@NoRepositoryBean
public interface BaseElasticSearchDao<T extends BaseElasticSearchModel, ID extends Serializable> extends ElasticsearchRepository<T, ID> {

}
