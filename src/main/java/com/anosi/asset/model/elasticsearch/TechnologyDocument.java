package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.Setting;


@Document(indexName="goaland",type="technologyDocument",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
@Setting(settingPath = "elasticsearch-analyser.json")
public class TechnologyDocument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259174821924357576L;
	
	@Field(index = FieldIndex.not_analyzed,store=true)
	private String id = UUID.randomUUID().toString();

	@Field(searchAnalyzer="ik_max_word",analyzer="ik_max_word")
	private String content;
	
	private String highLight;
	
	@Field(index = FieldIndex.not_analyzed,store=true)
	private String fileId;//文件的唯一id

	@Id
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

	public String getHighLight() {
		return highLight;
	}

	public void setHighLight(String highLight) {
		this.highLight = highLight;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
