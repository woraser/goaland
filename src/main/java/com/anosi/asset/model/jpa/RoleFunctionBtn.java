package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "roleFunctionBtn")
public class RoleFunctionBtn extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2198675367580201263L;
	
	private String name;

	private String btnId;
	
	private RoleFunction roleFunction;
	
	private List<Privilege> privilegeList = new ArrayList<Privilege>();
	
	private List<RoleFunctionGroup> roleFunctionGroupList = new ArrayList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBtnId() {
		return btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=RoleFunction.class)
	public RoleFunction getRoleFunction() {
		return roleFunction;
	}

	public void setRoleFunction(RoleFunction roleFunction) {
		this.roleFunction = roleFunction;
	}
	
	@ManyToMany(fetch=FetchType.LAZY,mappedBy="roleFunctionBtnList",targetEntity=Privilege.class)
	public List<Privilege> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<Privilege> privilegeList) {
		this.privilegeList = privilegeList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleFunctionBtnList", targetEntity = RoleFunctionGroup.class)
	public List<RoleFunctionGroup> getRoleFunctionGroupList() {
		return roleFunctionGroupList;
	}

	public void setRoleFunctionGroupList(List<RoleFunctionGroup> roleFunctionGroupList) {
		this.roleFunctionGroupList = roleFunctionGroupList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((btnId == null) ? 0 : btnId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleFunctionBtn other = (RoleFunctionBtn) obj;
		if (btnId == null) {
			if (other.btnId != null)
				return false;
		} else if (!btnId.equals(other.btnId))
			return false;
		return true;
	}
	
}
