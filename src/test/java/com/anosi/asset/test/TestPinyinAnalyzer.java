package com.anosi.asset.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.dao.elasticsearch.SearchRecordDao;
import com.anosi.asset.model.elasticsearch.SearchRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestPinyinAnalyzer {

	@Autowired
	private SearchRecordDao searchRecordDao;
	
	@Test
	public void testSave(){
		SearchRecord searchRecord = new SearchRecord();
		searchRecord.setSearchContent("腾讯游戏");
		searchRecordDao.index(searchRecord);
		//searchRecordDao.save(searchRecord);
	}
	
	@Test
	public void testSearch(){
		Page<SearchRecord> findBySearchContent = searchRecordDao.findBySearchContent("tx",new PageRequest(0, 5));
		findBySearchContent.getContent().forEach(s->System.out.println(s.getSearchContent()));
	}
	
}
