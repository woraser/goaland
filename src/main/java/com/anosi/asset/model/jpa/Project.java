package com.anosi.asset.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/***
 * 项目
 * @author jinyao
 *
 */
@Entity
@Table(name = "project")
public class Project extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8182240180903969356L;
	
	private String name;//项目名称
	
	private String number;//项目编号
	
	private String location;//项目地址

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
	
}
