package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "roleFunction")
public class RoleFunction extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4646361903857229821L;
	
	private String name;
	
	private String roleFunctionPageId;
	
	private RoleFunction parentRoleFunction;
	
	private List<RoleFunction> subRoleFunction = new ArrayList<RoleFunction>();
	
	private List<RoleFunctionBtn> roleFunctionBtnList = new ArrayList<RoleFunctionBtn>();
	
	private List<Privilege> privilegeList = new ArrayList<Privilege>();
	
	private List<RoleFunctionGroup> roleFunctionGroupList = new ArrayList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(unique=true,nullable=false)
	public String getRoleFunctionPageId() {
		return roleFunctionPageId;
	}

	public void setRoleFunctionPageId(String roleFunctionPageId) {
		this.roleFunctionPageId = roleFunctionPageId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pRoleFunctionId")
	public RoleFunction getParentRoleFunction() {
		return parentRoleFunction;
	}

	public void setParentRoleFunction(RoleFunction parentRoleFunction) {
		this.parentRoleFunction = parentRoleFunction;
	}

	@OneToMany(targetEntity = RoleFunction.class, cascade = {CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "parentRoleFunction")
	public List<RoleFunction> getSubRoleFunction() {
		return subRoleFunction;
	}

	public void setSubRoleFunction(List<RoleFunction> subRoleFunction) {
		this.subRoleFunction = subRoleFunction;
	}
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "roleFunction", targetEntity = RoleFunctionBtn.class)
	public List<RoleFunctionBtn> getRoleFunctionBtnList() {
		return roleFunctionBtnList;
	}

	public void setRoleFunctionBtnList(List<RoleFunctionBtn> roleFunctionBtnList) {
		this.roleFunctionBtnList = roleFunctionBtnList;
	}
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "roleFunction", targetEntity = Privilege.class)
	public List<Privilege> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<Privilege> privilegeList) {
		this.privilegeList = privilegeList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleFunctionList", targetEntity = RoleFunctionGroup.class)
	public List<RoleFunctionGroup> getRoleFunctionGroupList() {
		return roleFunctionGroupList;
	}

	public void setRoleFunctionGroupList(List<RoleFunctionGroup> roleFunctionGroupList) {
		this.roleFunctionGroupList = roleFunctionGroupList;
	}

	@Transient
	public boolean isFirstNode(){
		return parentRoleFunction==null;
	}

}
