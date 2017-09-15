package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/****
 * 物料
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "materiel",uniqueConstraints = {@UniqueConstraint(columnNames={"name", "device_id"})})
public class Materiel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6709768657814585431L;

	private String name;

	private Device device;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="device_id")
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
