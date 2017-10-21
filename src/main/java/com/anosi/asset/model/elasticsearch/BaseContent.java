package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class BaseContent extends BaseElasticSearchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5430597379923484984L;

	@Id
	private String id;

	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word", store = true)
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
