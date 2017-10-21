package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "goaland", type = "searchRecord", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
@Setting(settingPath = "elasticsearch-analyser.json")
public class SearchRecord extends BaseElasticSearchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3416052213981587587L;

	@Id
	private String id = UUID.randomUUID().toString();

	@Field(type = FieldType.String, searchAnalyzer = "lc_search", analyzer = "lc_index", store = true)
	private String searchContent;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((searchContent == null) ? 0 : searchContent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchRecord other = (SearchRecord) obj;
		if (searchContent == null) {
			if (other.searchContent != null)
				return false;
		} else if (!searchContent.equals(other.searchContent))
			return false;
		return true;
	}

}
