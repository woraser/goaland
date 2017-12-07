package com.anosi.asset.dao.elasticsearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.anosi.asset.model.elasticsearch.TechnologyDocument;

public interface TechnologyDocumentDao extends BaseElasticSearchDao<TechnologyDocument, String> {

	public List<TechnologyDocument> findByTypeEquals(String type);

	/***
	 * 查找TechnologyDocument,带有匹配高亮
	 * 
	 * @param elasticsearchTemplate
	 * @param searchQuery
	 * @return
	 */
	default public Page<TechnologyDocument> getHighLight(ElasticsearchTemplate elasticsearchTemplate,
			NativeSearchQueryBuilder queryBuilder) {
		Field fieldConent = new HighlightBuilder.Field("content");
		fieldConent.preTags("<font color='#FF0000'>");
		fieldConent.postTags("</font>");

		Field fieldFileName = new HighlightBuilder.Field("fileName");
		fieldFileName.preTags("<font color='#FF0000'>");
		fieldFileName.postTags("</font>");

		SearchQuery searchQuery = queryBuilder.withHighlightFields(fieldConent, fieldFileName).build();

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
							
							// 实际的内容
							String realContent = (String) searchHit.getSource().get("content");
							// 高亮内容
							HighlightField content = searchHit.getHighlightFields().get("content");
							StringBuilder highLightContent = new StringBuilder();
							if (content != null) {
								Text[] hightLightContents = content.fragments();
								for (Text t : hightLightContents) {
									highLightContent.append(".......");
									highLightContent.append(t.toString());
									highLightContent.append(".......");
									highLightContent.append("\t");
								}
								technologyDocument.setHighLightContent(highLightContent.toString());
							} else if(StringUtils.isNoneBlank(realContent)){
								StringBuilder cutOut = new StringBuilder();
								// 判断长度进行截取
								if (realContent.length() < 100) {
									cutOut.append(realContent);
								} else {
									cutOut.append(realContent.substring(0, 100));
								}
								cutOut.append(".......");
								// 如果没有高亮内容，则在文件内容中截取一段代替
								technologyDocument.setHighLightContent(cutOut.toString());
								// 截取一段内容作为展示
								technologyDocument.setContent(cutOut.toString());
							}

							// 高亮标题
							HighlightField fileName = searchHit.getHighlightFields().get("fileName");
							StringBuilder highLightFileName = new StringBuilder();
							if (fileName != null) {
								Text[] hightLightFileNames = fileName.fragments();
								if (hightLightFileNames != null && hightLightFileNames.length != 0) {
									highLightFileName.append(hightLightFileNames[0].toString());
								}
								technologyDocument.setHighLightFileName(highLightFileName.toString());
							} else {
								// 如果没有高亮标题，就用文件名代替
								technologyDocument.setHighLightFileName((String) searchHit.getSource().get("fileName"));
							}

							// 设置需要展示的属性
							technologyDocument.setId(searchHit.getId());
							technologyDocument.setFileId((String) searchHit.getSource().get("fileId"));
							technologyDocument.setFileName((String) searchHit.getSource().get("fileName"));
							technologyDocument.setType((String) searchHit.getSource().get("type"));
							technologyDocument.setUploader((String) searchHit.getSource().get("uploader"));
							technologyDocument.setUploaderName((String) searchHit.getSource().get("uploaderName"));
							technologyDocument.setUploadTime(new Date((Long) searchHit.getSource().get("uploadTime")));
							technologyDocument.setFileSize(((Number) searchHit.getSource().get("fileSize")).longValue());

							chunk.add(technologyDocument);
						}
						return new AggregatedPageImpl<>((List<T>) chunk, pageable, response.getHits().getTotalHits());
					}
				});

		return queryForPage;
	}

}
