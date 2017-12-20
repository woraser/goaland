package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 领导审批字段
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "examineDetail")
public class ExamineDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5712628425612098192L;

	private boolean reject = false;// 领导驳回

	private String suggestion;// 领导审批意见

	private Account engineeDep;// 工程部评估人

	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;

	public boolean getReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}

	@Type(type = "text")
	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getEngineeDep() {
		return engineeDep;
	}

	public void setEngineeDep(Account engineeDep) {
		this.engineeDep = engineeDep;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "examineDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}

}
