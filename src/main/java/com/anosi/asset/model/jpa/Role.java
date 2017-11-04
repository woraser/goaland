package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7714418401452387106L;

	@Field
	private String name;

	private String code;

	@IndexedEmbedded
	private DepGroup depGroup;

	@ContainedIn
	private List<Account> accountList = new ArrayList<Account>();

	@Column(unique = true, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleList", targetEntity = Account.class)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Role other = (Role) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
}
