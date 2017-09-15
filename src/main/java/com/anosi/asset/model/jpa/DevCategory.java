package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "devCategory")
public class DevCategory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7974456468757773048L;

	private String name;// 名字目前就只有5种

	private List<Device> deviceList = new ArrayList<>();

	private List<DevCategoryStructures> mainDevCategoryStructures = new ArrayList<>();

	private List<DevCategoryStructures> subDevCategoryStructures = new ArrayList<>();

	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(targetEntity = Device.class, cascade = {
			CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "devCategory")
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "mainDevCategory", targetEntity = DevCategoryStructures.class)
	public List<DevCategoryStructures> getMainDevCategoryStructures() {
		return mainDevCategoryStructures;
	}

	public void setMainDevCategoryStructures(List<DevCategoryStructures> mainDevCategoryStructures) {
		this.mainDevCategoryStructures = mainDevCategoryStructures;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "subDevCategory", targetEntity = DevCategoryStructures.class)
	public List<DevCategoryStructures> getSubDevCategoryStructures() {
		return subDevCategoryStructures;
	}

	public void setSubDevCategoryStructures(List<DevCategoryStructures> subDevCategoryStructures) {
		this.subDevCategoryStructures = subDevCategoryStructures;
	}

}
