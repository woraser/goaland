package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roleFunctionGroup")
public class RoleFunctionGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1854144739609018821L;

	private String name;

	private List<RoleFunction> roleFunctionList = new ArrayList<>();

	private List<RoleFunctionBtn> roleFunctionBtnList = new ArrayList<>();

	private List<Account> accountList = new ArrayList<>();

	@Column(unique=true,nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<RoleFunction> getRoleFunctionList() {
		return roleFunctionList;
	}

	public void setRoleFunctionList(List<RoleFunction> roleFunctionList) {
		this.roleFunctionList = roleFunctionList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<RoleFunctionBtn> getRoleFunctionBtnList() {
		return roleFunctionBtnList;
	}

	public void setRoleFunctionBtnList(List<RoleFunctionBtn> roleFunctionBtnList) {
		this.roleFunctionBtnList = roleFunctionBtnList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleFunctionGroupList", targetEntity = Account.class)
	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

}
