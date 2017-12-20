package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 派单字段
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "distributeDetail")
public class DistributeDetail extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5996500061650214805L;

	private Account engineer;// 工程师
	
	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getEngineer() {
		return engineer;
	}

	public void setEngineer(Account engineer) {
		this.engineer = engineer;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "distributeDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}
	
}
