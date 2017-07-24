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
@Table(name = "role")
public class Role extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7714418401452387106L;
	
	private String name;
	
	private String code;
	
	private DepGroup depGroup;
	
	private List<Account> accountList = new ArrayList<Account>();

	@Column(unique=true,nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "role", targetEntity = Account.class)
	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}
	@ManyToOne(fetch = FetchType.LAZY) 
	public DepGroup getDepGroup() {
		return depGroup;
	}

	public void setDepGroup(DepGroup depGroup) {
		this.depGroup = depGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
