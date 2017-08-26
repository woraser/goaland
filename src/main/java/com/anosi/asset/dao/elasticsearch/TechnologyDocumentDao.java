package com.anosi.asset.dao.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.anosi.asset.model.elasticsearch.TechnologyDocument;

public interface TechnologyDocumentDao extends ElasticsearchRepository<TechnologyDocument, String> {
	
	public List<TechnologyDocument> findByTypeEquals(String type);

	/***
	 * 查找TechnologyDocument,带有匹配高亮
	 * 
	 * @param elasticsearchTemplate
	 * @param searchQuery
	 * @return
	 */
	default public Page<TechnologyDocument> getHighLightContent(ElasticsearchTemplate elasticsearchTemplate,
			NativeSearchQueryBuilder queryBuilder) {
		Field field = new HighlightBuilder.Field("content");
		field.preTags("<font color='#FF0000'>");
		field.postTags("</font>");

		SearchQuery searchQuery = queryBuilder.withHighlightFields(field).build();

		AggregatedPage<TechnologyDocument> queryForPage = elasticsearchTemplate.queryForPage(searchQuery,
				TechnologyDocument.class, new SearchResultMapper() {

					@SuppressWarnings("unchecked")
					@Override
					public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz,
							Pageable pageable) {
						List<TechnologyDocument> chunk = new ArrayList<>();
						for (SearchHit searchHit : response.getHits()) {
							if (response.getHits().getHits().length <= 0) {
								return null;
							}
							TechnologyDocument technologyDocument = new TechnologyDocument();
							Text[] fragments = searchHit.getHighlightFields().get("content").fragments();
							StringBuilder highLight = new StringBuilder();
							for(Text t : fragments){
								highLight.append(".......");
								highLight.append(t.toString());
								highLight.append(".......");
								highLight.append("\t");
							}
							technologyDocument.setId(searchHit.getId());
							technologyDocument.setFileId((String) searchHit.getSource().get("fileId"));
							technologyDocument.setHighLight(highLight.toString());
							chunk.add(technologyDocument);
						}
						return new AggregatedPageImpl<>((List<T>) chunk, pageable, response.getHits().getTotalHits());
					}
				});

		return queryForPage;
	}

}
