package com.anosi.asset.test;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.dao.elasticsearch.RepairDocumentDao;
import com.anosi.asset.model.elasticsearch.RepairDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class ElasticsearchTest {

	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchTest.class);
	
	@Autowired
	private RepairDocumentDao repairDocumentDao;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void testSave(){
		RepairDocument repairDocument = new RepairDocument();
		repairDocument.setId(UUID.randomUUID().toString());
		repairDocument.setProblemDescription("水机漏水");
		repairDocument.setFailureCause("年久失修");
		repairDocument.setProcessMode("返厂维修");
		repairDocumentDao.save(repairDocument);
	}
	
	@Test
	public void testFind(){
		List<RepairDocument> repairDocuments = repairDocumentDao.findByProblemDescriptionOrFailureCauseOrProcessMode("水机", "", "返厂");
		repairDocuments.stream().forEach(repairDocument->System.out.println(repairDocument.getProblemDescription()));
	}
	
	@Test
	public void testHighLight(){
		Field field = new HighlightBuilder.Field("problemDescription");
		field.preTags("<b>");
		field.postTags("</b>");
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery("problemDescription", "漏水"))
				.withHighlightFields(field).build();
		
		AggregatedPage<RepairDocument> queryForPage = elasticsearchTemplate.queryForPage(searchQuery, RepairDocument.class, new SearchResultMapper() {
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				List<RepairDocument> chunk = new ArrayList<>();
				for (SearchHit searchHit : response.getHits()) {
					if (response.getHits().getHits().length <= 0) {
						return null;
					}
					RepairDocument repairDocument = new RepairDocument();
					String highlight = searchHit.getHighlightFields().get("problemDescription").fragments()[0].toString();
					repairDocument.setProblemDescription(highlight);
					chunk.add(repairDocument);
				}
				return new AggregatedPageImpl<>((List<T>) chunk);
			}
		});
		
		for (RepairDocument repairDocument : queryForPage) {
			logger.debug("highlight:{}",repairDocument.getProblemDescription());
		}
	}
	
}
