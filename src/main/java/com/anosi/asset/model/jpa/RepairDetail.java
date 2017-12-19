package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/***
 * 维修的表单字段
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "repairDetail")
public class RepairDetail extends BaseRepairDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4786482777775582115L;

	private boolean entrust;

	private CustomerServiceProcess customerServiceProcess;
	
	private Account entruster;
	
	private List<Account> fellowList = new ArrayList<>();
	
	private List<Device> deviceList = new ArrayList<>();

	public boolean getEntrust() {
		return entrust;
	}

	public void setEntrust(boolean entrust) {
		this.entrust = entrust;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "repairDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getEntruster() {
		return entruster;
	}

	public void setEntruster(Account entruster) {
		this.entruster = entruster;
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
	
}
