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
	
	private BigInteger preview;//预览文件的唯一id

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
	
	public BigInteger getPreview() {
		return preview;
	}

	public void setPreview(BigInteger preview) {
		this.preview = preview;
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
	
	public String getSuffix() {
		return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
	}

	@Override
	public String toString() {
		return "FileMetaData [identification=" + identification + ", uploader=" + uploader + ", uploadTime="
				+ uploadTime + ", fileName=" + fileName + ", fileSize=" + fileSize + ", objectId=" + objectId + "]";
	}
	
	/***
	 * 由于前端不能识别bigInteger这种大数字，需要提供一个objectId的string版本
	 * 
	 * @return
	 */
	public String getStringObjectId(){
		if(objectId!=null){
			return objectId.toString();
		}
		return null;
	}
	
	/***
	 * 返回文件大小 -h
	 * 
	 * @return
	 */
	public String getFileSizeH(){
		//如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义  
	    if (fileSize < 1024) {  
	        return String.valueOf(fileSize) + "B";  
	    } else {  
	        fileSize = fileSize / 1024;  
	    }  
	    //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位  
	    //因为还没有到达要使用另一个单位的时候  
	    //接下去以此类推  
	    if (fileSize < 1024) {  
	        return String.valueOf(fileSize) + "KB";  
	    } else {  
	        fileSize = fileSize / 1024;  
	    }  
	    if (fileSize < 1024) {  
	        //因为如果以MB为单位的话，要保留最后1位小数，  
	        //因此，把此数乘以100之后再取余  
	        fileSize = fileSize * 100;  
	        return String.valueOf((fileSize / 100)) + "."  
	                + String.valueOf((fileSize % 100)) + "MB";  
	    } else {  
	        //否则如果要以GB为单位的，先除于1024再作同样的处理  
	        fileSize = fileSize * 100 / 1024;  
	        return String.valueOf((fileSize / 100)) + "."  
	                + String.valueOf((fileSize % 100)) + "GB";  
	    }  
	}

}
