package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "goaland", type = "searchRecord", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
@Setting(settingPath = "elasticsearch-analyser.json")
public class SearchRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3416052213981587587L;

	@Field(index = FieldIndex.not_analyzed,store=true)
	private String id = UUID.randomUUID().toString();

	@Field(searchAnalyzer = "lc_search", analyzer = "lc_index",store=true)
	private String searchContent;

	@Field(index = FieldIndex.not_analyzed,store=true)
	private Integer searchCount = 1;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

}
