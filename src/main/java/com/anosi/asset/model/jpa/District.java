package com.anosi.asset.model.jpa;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "district")
public class District extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7555992276120785280L;
	
	private String name;

	private City city;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne( fetch = FetchType.LAZY) 
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
}
