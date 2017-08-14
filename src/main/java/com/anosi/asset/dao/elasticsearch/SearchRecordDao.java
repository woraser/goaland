package com.anosi.asset.dao.elasticsearch;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.anosi.asset.model.elasticsearch.SearchRecord;

public interface SearchRecordDao extends ElasticsearchRepository<SearchRecord, String>{

	public List<SearchRecord> findBySearchContent(String searchContent,Pageable pageable);
	
}
