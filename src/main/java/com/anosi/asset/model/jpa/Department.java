package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="department")
public class Department extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7980380972329240770L;

	private String name;
	
	private String code;
	
	private List<DepGroup> depGroupList = new ArrayList<>();
	
	private Company company;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "department", targetEntity = DepGroup.class)
	public List<DepGroup> getDepGroupList() {
		return depGroupList;
	}

	public void setDepGroupList(List<DepGroup> depGroupList) {
		this.depGroupList = depGroupList;
	}
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Company.class)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	@Column(unique=true,nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
