package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.elasticsearch.SearchRecordDao;
import com.anosi.asset.model.elasticsearch.SearchRecord;
import com.anosi.asset.service.SearchRecordService;

@Service("searchRecordService")
@Transactional
public class SearchRecordServiceImpl implements SearchRecordService{

	@Autowired
	private SearchRecordDao searchRecordDao;

	@Override
	public Page<SearchRecord> findBySearchContent(String searchContent, Pageable pageable) {
		return searchRecordDao.findBySearchContent(searchContent, pageable);
	}
	
}
