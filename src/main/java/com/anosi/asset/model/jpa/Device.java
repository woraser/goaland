package com.anosi.asset.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class Device extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 587030319812196854L;
	
	private String serialNo;

	@Column(unique=true,nullable=false)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
}
