package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 工程师评估字段
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "evaluatingDetail")
public class EvaluatingDetail extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2202554349113232657L;

	private String breakdownDevice;// 故障评估

	private Account servicer;// 服务组人员
	
	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;

	@Type(type="text")
	public String getBreakdownDevice() {
		return breakdownDevice;
	}

	public void setBreakdownDevice(String breakdownDevice) {
		this.breakdownDevice = breakdownDevice;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getServicer() {
		return servicer;
	}

	public void setServicer(Account servicer) {
		this.servicer = servicer;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "evaluatingDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}
	
}
