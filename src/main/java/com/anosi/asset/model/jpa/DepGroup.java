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

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
@Table(name="depGroup")
public class DepGroup extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5843865967569372213L;

	@Field
	private String name;
	
	private String code;
	
	@IndexedEmbedded
	private Department department;
	
	@ContainedIn
	private List<Role> roleList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(unique=true,nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@ManyToOne(fetch = FetchType.LAZY) 
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "depGroup", targetEntity = Role.class)
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
}
