package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 项目
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8182240180903969356L;

	@Field
	private String name;// 项目名称

	@Field(analyze = Analyze.NO)
	private String number;// 项目编号

	@Field
	private String location;// 项目地址

	@JSONField(serialize=false)  
	@ContainedIn
	private List<CustomerServiceProcess> customerServiceProcesseList = new ArrayList<>();

	@JSONField(serialize=false)  
	@ContainedIn
	private List<Device> deviceList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true, nullable = false)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "project", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getCustomerServiceProcesseList() {
		return customerServiceProcesseList;
	}

	public void setCustomerServiceProcesseList(List<CustomerServiceProcess> customerServiceProcesseList) {
		this.customerServiceProcesseList = customerServiceProcesseList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "project", targetEntity = Device.class)
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

}
