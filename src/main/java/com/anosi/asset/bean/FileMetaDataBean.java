package com.anosi.asset.bean;

import java.io.InputStream;
import java.io.Serializable;

public class FileMetaDataBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 380751393896299274L;

	private String identification;
	
	private String fileName;
	
	private InputStream is;
	
	private Long fileSize;
	
	public FileMetaDataBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileMetaDataBean(String identification, String fileName, InputStream is, Long fileSize) {
		super();
		this.identification = identification;
		this.fileName = fileName;
		this.is = is;
		this.fileSize = fileSize;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

}
