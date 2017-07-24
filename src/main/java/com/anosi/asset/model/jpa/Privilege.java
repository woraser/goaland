package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "privilege")
public class Privilege extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2903529238831749451L;

	private Account account;
	
	private RoleFunction roleFunction;
	
	private List<RoleFunctionBtn> roleFunctionBtnList = new ArrayList<RoleFunctionBtn>();

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=RoleFunction.class)
	public RoleFunction getRoleFunction() {
		return roleFunction;
	}

	public void setRoleFunction(RoleFunction roleFunction) {
		this.roleFunction = roleFunction;
	}

	@ManyToMany(fetch=FetchType.LAZY,targetEntity=RoleFunctionBtn.class)
	public List<RoleFunctionBtn> getRoleFunctionBtnList() {
		return roleFunctionBtnList;
	}

	public void setRoleFunctionBtnList(List<RoleFunctionBtn> roleFunctionBtnList) {
		this.roleFunctionBtnList = roleFunctionBtnList;
	}
	
	
	
}
