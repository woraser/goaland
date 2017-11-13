package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "repairedDeviceDailyPer")
public class RepairedDeviceDailyPer extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2589732036410976548L;

	private Date countDate;

	private Long repaired;

	private DevCategory devCategory;
	
	private List<Device> deviceList = new ArrayList<>();

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public Long getRepaired() {
		return repaired;
	}

	public void setRepaired(Long repaired) {
		this.repaired = repaired;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DevCategory.class)
	public DevCategory getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(DevCategory devCategory) {
		this.devCategory = devCategory;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}
	
}
