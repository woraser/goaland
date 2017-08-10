package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName="goaland",type="technologyDocument",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class TechnologyDocument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259174821924357576L;
	
	private String id = UUID.randomUUID().toString();

	private String content;
	
	private String highLight;
	
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
