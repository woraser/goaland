package com.anosi.asset.test

import static org.elasticsearch.index.query.QueryBuilders.termQuery

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import com.anosi.asset.GoalandApplication
import com.anosi.asset.model.elasticsearch.TechnologyDocument
import com.anosi.asset.service.TechnologyDocumentService

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
class TestTechnologyDocument {

	@Autowired
	private TechnologyDocumentService technologyDocumentService;

	@Test
	void testCreate(){
		technologyDocumentService.createTechnologyDocument(new File('E:/水源产品生命周期跟踪系统技术文档7.11.docx'),"故障文档")
	}

	@Test
	void testSearch(){
		Page<TechnologyDocument> pages = technologyDocumentService.getHighLightContent("天气", new PageRequest(0, 10))
		pages.getContent().each{
			println it.highLight
			println it.fileId
		}
	}

	static class SearchHelper{
		static Page<TechnologyDocument> search(TechnologyDocumentService self){
			NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(termQuery("content", "天气")).withPageable(new PageRequest(0, 10))
			return self.technologyDocumentDao.getHighLightContent(self.elasticsearchTemplate,queryBuilder)
		}
	}
}
