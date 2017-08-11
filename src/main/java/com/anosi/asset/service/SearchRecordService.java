package com.anosi.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.elasticsearch.SearchRecord;

public interface SearchRecordService {

	public Page<SearchRecord> findBySearchContent(String searchContent,Pageable pageable);
	
}
