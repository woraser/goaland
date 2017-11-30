package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Entity
@Table(name = "advertisement")
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class Advertisement extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6509559856973919178L;

	@Field
	private String name;// 广告名字

	@IndexedEmbedded
	private Account creater;// 创建人

	private Date createTime = new Date();// 创建时间

	private Date sendTime;// 发送时间
	
	private String coverPictureId;// 轮播图id
	
	private String unPubishContent;// 未发布的内容
	
	private String unPubishHtmlContent;// 未发布的html内容

	@Field
	private String content;// 不带html的内容
	
	private String htmlContent;// html内容

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Type(type="text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getCreater() {
		return creater;
	}

	public void setCreater(Account creater) {
		this.creater = creater;
	}

	public String getCoverPictureId() {
		return coverPictureId;
	}

	public void setCoverPictureId(String coverPictureId) {
		this.coverPictureId = coverPictureId;
	}

	@Type(type="text")
	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	@Type(type="text")
	public String getUnPubishContent() {
		return unPubishContent;
	}

	public void setUnPubishContent(String unPubishContent) {
		this.unPubishContent = unPubishContent;
	}

	@Type(type="text")
	public String getUnPubishHtmlContent() {
		return unPubishHtmlContent;
	}

	public void setUnPubishHtmlContent(String unPubishHtmlContent) {
		this.unPubishHtmlContent = unPubishHtmlContent;
	}
	
}
