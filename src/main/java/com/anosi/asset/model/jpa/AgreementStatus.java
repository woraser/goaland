package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 合同状态
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "agreementStatus")
public class AgreementStatus extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1183185189162951919L;

	private Date beginTime;

	private Date endTime;

	private Agreement agreement;

	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;

	public static enum Agreement {
		UNDERGUARANTEE, BEYONDGUARANTEE, UNCOMFIRMED, COMFIRMED;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Agreement getAgreement() {
		return agreement;
	}

	public void setAgreement(Agreement agreement) {
		this.agreement = agreement;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "agreementStatus")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}

	@Transient
	public Agreement checkAgreement() {
		if (endTime == null) {
			return Agreement.UNCOMFIRMED;
		} else {
			if (customerServiceProcess.getCreateDate().getTime() <= endTime.getTime()) {
				return Agreement.UNDERGUARANTEE;
			} else {
				return Agreement.BEYONDGUARANTEE;
			}
		}
	}

}
