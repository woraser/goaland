package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 委派的详情
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "entrustDetail")
public class EntrustDetail extends BaseRepairDetail{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9184539580181895001L;

	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;
	
	private List<Account> fellowList = new ArrayList<>();
	
	private List<Device> deviceList = new ArrayList<>();
	
	private List<FaultCategory> faultCategoryList = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "entrustDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<Account> getFellowList() {
		return fellowList;
	}

	public void setFellowList(List<Account> fellowList) {
		this.fellowList = fellowList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<FaultCategory> getFaultCategoryList() {
		return faultCategoryList;
	}

	public void setFaultCategoryList(List<FaultCategory> faultCategoryList) {
		this.faultCategoryList = faultCategoryList;
	}
	
}
