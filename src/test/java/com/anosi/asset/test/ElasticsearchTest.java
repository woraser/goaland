package com.anosi.asset.test;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;
import java.util.List;

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
import com.anosi.asset.model.elasticsearch.TechnologyDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class ElasticsearchTest {

	
	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchTest.class);
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void testHighLight(){
		Field field = new HighlightBuilder.Field("content");
		field.preTags("<b>");
		field.postTags("</b>");
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery("content", "测试"))
				.withHighlightFields(field).build();
		
		AggregatedPage<TechnologyDocument> queryForPage = elasticsearchTemplate.queryForPage(searchQuery, TechnologyDocument.class, new SearchResultMapper() {
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				List<TechnologyDocument> chunk = new ArrayList<>();
				for (SearchHit searchHit : response.getHits()) {
					if (response.getHits().getHits().length <= 0) {
						return null;
					}
					TechnologyDocument technologyDocument = new TechnologyDocument();
					String highLight = searchHit.getHighlightFields().get("content").fragments()[0].toString();
					technologyDocument.setHighLight(highLight);
					chunk.add(technologyDocument);
				}
				return new AggregatedPageImpl<>((List<T>) chunk);
			}
		});
		
		queryForPage.getContent().forEach(s->logger.debug(s.getHighLight()));
		
	}
	
}
