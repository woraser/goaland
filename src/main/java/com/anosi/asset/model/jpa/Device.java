package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class Device extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 587030319812196854L;
	
	private String serialNo;
	
	private DevCategory devCategory;
	
	private Device parentDevice;
	
	private List<Device> subDevices = new ArrayList<>();

	@Column(unique=true,nullable=false)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pDeviceId")
	public Device getParentDevice() {
		return parentDevice;
	}

	public void setParentDevice(Device parentDevice) {
		this.parentDevice = parentDevice;
	}

	@OneToMany(targetEntity = Device.class, cascade = {CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "parentDevice")
	public List<Device> getSubDevices() {
		return subDevices;
	}

	public void setSubDevices(List<Device> subDevices) {
		this.subDevices = subDevices;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=DevCategory.class)
	@JoinColumn(nullable=false)
	public DevCategory getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(DevCategory devCategory) {
		this.devCategory = devCategory;
	}
	
}
