package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "messageInfo")
public class MessageInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2676027100413861771L;
	
	private Account from;
	
	private Account to;
	
	private Date readTime;
	
	private String content;
	
	private String title;
	
	private String url;
	
	private MessageType messageType = MessageType.DEFAULT;
	
	//信息的种类，内部枚举类
	public static enum MessageType{
		
		DEFAULT("默认"),INFO("普通信息"),PRIMARY("私人信息"),SUCCESS("成功"),WARNING("警告"),DANGER("危险");
		
		private String type;

		private MessageType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
	}
	
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	@JoinColumn(nullable=false)
	public Account getFrom() {
		return from;
	}

	public void setFrom(Account from) {
		this.from = from;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	@JoinColumn(nullable=false)
	public Account getTo() {
		return to;
	}

	public void setTo(Account to) {
		this.to = to;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Transient
	public boolean isRead(){
		return readTime!=null;
	}
	
}
