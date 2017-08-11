package com.anosi.asset.model.mongo;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.annotation.JSONField;

@Document
public class FileMetaData extends AbstractDocument{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2638537610522555992L;

	@Indexed
	private String identification;//组标识，下次可以通过标识找到相应的fileList
	
	private String uploader;
	
	private Date uploadTime;
	
	private String fileName;
	
	private Long fileSize;
	
	@Indexed(unique=true)
	private BigInteger objectId;//文件的唯一id

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
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

	public BigInteger getObjectId() {
		return objectId;
	}

	public void setObjectId(BigInteger objectId) {
		this.objectId = objectId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	@Override
	public String toString() {
		return "FileMetaData [identification=" + identification + ", uploader=" + uploader + ", uploadTime="
				+ uploadTime + ", fileName=" + fileName + ", fileSize=" + fileSize + ", objectId=" + objectId + "]";
	}

}
