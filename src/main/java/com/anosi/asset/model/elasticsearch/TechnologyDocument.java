package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.anosi.asset.util.FormatUtil;

@Document(indexName = "goaland", type = "technologyDocument", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
@Setting(settingPath = "elasticsearch-analyser.json")
public class TechnologyDocument extends BaseElasticSearchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259174821924357576L;

	@Id
	private String id = UUID.randomUUID().toString();

	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word", store = true)
	private String content;

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String fileId;// 文件的唯一id

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String type;

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String uploader;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String uploaderName;

	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true)
	private Date uploadTime;

	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word", store = true)
	private String fileName;

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String suffix;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String identification;
	
	@Field(type = FieldType.Long, index = FieldIndex.not_analyzed, store = true)
	private Long fileSize;

	private String highLightContent;

	private String highLightFileName;

	// 上传时间下限
	private Date lowerLimit;

	// 上传时间上限
	private Date upperLimit;

	private String searchContent;

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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHighLightContent() {
		return highLightContent;
	}

	public void setHighLightContent(String highLightContent) {
		this.highLightContent = highLightContent;
	}

	public String getHighLightFileName() {
		return highLightFileName;
	}

	public void setHighLightFileName(String highLightFileName) {
		this.highLightFileName = highLightFileName;
	}

	public Date getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Date lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Date getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Date upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getSuffix() {
		return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getUploaderName() {
		return uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/***
	 * 返回文件大小 -h
	 * 
	 * @return
	 */
	public String getFileSizeH() {
		return FormatUtil.getFileSizeH(fileSize);
	}
	
}
