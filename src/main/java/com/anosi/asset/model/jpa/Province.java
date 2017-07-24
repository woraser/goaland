package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "province")
public class Province extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9184198336679250150L;

	private String name;
	
	private String pid;
	
	private List<City> cityList = new ArrayList<City>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@OneToMany(mappedBy="province", cascade={CascadeType.MERGE}, fetch = FetchType.LAZY)  
	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
