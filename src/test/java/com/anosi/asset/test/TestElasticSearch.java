package com.anosi.asset.test;

import java.util.Date;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.dao.elasticsearch.TechnologyDocumentDao;
import com.anosi.asset.model.elasticsearch.TechnologyDocument;
import com.anosi.asset.service.TechnologyDocumentService;
import com.anosi.asset.util.DateFormatUtil;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestElasticSearch {

	@Autowired
	private TechnologyDocumentService technologyDocumentService;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private TechnologyDocumentDao technologyDocumentDao;

	@Test
	public void testSearch() {
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

	@Test
	public void testMultiType() throws Exception {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withPageable(new PageRequest(0, 20));

		BoolQueryBuilder boolQueryBuilder = boolQuery().should(termQuery("type", "技术文档"))
				.should(termQuery("type", "故障文档")).must(termQuery("uploader", "gcbjl"));
		queryBuilder.withFilter(boolQueryBuilder);
		Page<TechnologyDocument> pages = technologyDocumentDao.getHighLight(elasticsearchTemplate, queryBuilder);
		for (TechnologyDocument td : pages) {
			//System.out.println(td.getHighLightFileName());
			//System.out.println(td.getHighLightContent());
			System.out.println(td.getUploader());
			System.out.println(td.getType());
		}
	}

}
