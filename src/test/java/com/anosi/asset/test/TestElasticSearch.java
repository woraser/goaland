package com.anosi.asset.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.model.elasticsearch.TechnologyDocument;
import com.anosi.asset.service.TechnologyDocumentService;
import com.anosi.asset.util.DateFormatUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestElasticSearch {

	@Autowired
	private TechnologyDocumentService technologyDocumentService;
	
	@Test
	public void testSearch(){
		TechnologyDocument technologyDocument = new TechnologyDocument();
		technologyDocument.setSearchContent("文档");
		technologyDocument.setType("故障文档");
		technologyDocument.setUploader("admin");
		technologyDocument.setUpperLimit(new Date());
		technologyDocument.setLowerLimit(DateFormatUtil.getDateByParttern("2017-08-28 10:00:00"));
		Page<TechnologyDocument> pages;
		try {
			pages = technologyDocumentService.getHighLight(technologyDocument, new PageRequest(0, 10));
			for (TechnologyDocument td : pages) {
				System.out.println(td.getHighLightFileName());
				System.out.println(td.getHighLightContent());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
