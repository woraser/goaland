package com.anosi.asset.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.elasticsearch.BaseElasticSearchDao;
import com.anosi.asset.dao.elasticsearch.SearchRecordDao;
import com.anosi.asset.model.elasticsearch.SearchRecord;
import com.anosi.asset.service.SearchRecordService;
import com.anosi.asset.util.PinyinUtil;

@Service("searchRecordService")
@Transactional
public class SearchRecordServiceImpl extends BaseElasticSearchServiceImpl<SearchRecord, String>
		implements SearchRecordService {

	@Autowired
	private SearchRecordDao searchRecordDao;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private int centerLimit = 5;

	@Override
	public BaseElasticSearchDao<SearchRecord, String> getRepository() {
		return searchRecordDao;
	}

	@Override
	public List<SearchRecord> findBySearchContent(String searchContent, Pageable pageable) {
		// 先查询个人词库，再查询中央词库
		List<SearchRecord> locals = findLocal(searchContent, "search_" + sessionComponent.getCurrentUser().getLoginId(),
				pageable);
		if (locals.size() < pageable.getPageSize()) {
			List<SearchRecord> centers = findCenter(searchContent, new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize() - locals.size(), pageable.getSort()));
			locals.addAll(centers);
		}
		return locals.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<SearchRecord> findLocal(String searchContent, String key, Pageable pageable) {
		SetOperations<String, String> vo = redisTemplate.opsForSet();
		Set<String> members = vo.members(key);
		return members.stream().map(m -> {
			// 以searchContent开头 或者 拼音以searchContent开头 或者 拼音首字母已searchContent开头
			// m是members中每一个元素,也就是个人词库中的每一个词
			if (m.startsWith(searchContent)
					|| (PinyinUtil.getStringPinYin(m).length != 0
							&& PinyinUtil.getStringPinYin(m)[0].startsWith(searchContent))
					|| (PinyinUtil.getStringPinYinPrefix(m).length != 0
							&& PinyinUtil.getStringPinYinPrefix(m)[0].startsWith(searchContent))) {
				SearchRecord searchRecord = new SearchRecord();
				searchRecord.setSearchContent(m);
				return searchRecord;
			}
			return null;
		}).filter(searchRecord -> searchRecord != null).limit(pageable.getPageSize()).collect(Collectors.toList());
	}

	@Override
	public List<SearchRecord> findCenter(String searchContent, Pageable pageable) {
		return searchRecordDao.findBySearchContent(searchContent, pageable);
	}

	@Override
	@Async
	public void insertInto(String searchContent, String account) {
		if (insetIntoLocal(searchContent, account)) {
			insertIntoCenter(searchContent);
		}
	}

	@Override
	public boolean insetIntoLocal(String searchContent, String account) {
		// 为了效率，维护了两个set
		SetOperations<String, String> vo = redisTemplate.opsForSet();
		Long lastSize = vo.size(searchContent);
		vo.add(searchContent, account);
		vo.add(account, searchContent);
		Long nowSize = vo.size(searchContent);
		return lastSize < centerLimit && nowSize == centerLimit;
	}

	@Override
	public void insertIntoCenter(String searchContent) {
		SearchRecord searchRecord = new SearchRecord();
		searchRecord.setSearchContent(searchContent);
		searchRecordDao.save(searchRecord);
	}

	@Override
	public void clearLocal(String account) {
		redisTemplate.delete(account);
	}

}
