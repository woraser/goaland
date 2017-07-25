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
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "account")
public class Account extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1503690620448714787L;
	
	private String name;
	
	private String loginId;

	private String password;
	
	private Role role;

	private List<Privilege> privilegeList = new ArrayList<>();
	
	private List<MessageInfo> formMessageList = new ArrayList<>();

	private List<MessageInfo> toMessageList = new ArrayList<>();
	
	//密码加盐
	private String salt;
	
	@Column(unique=true,nullable=false)
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@JSONField(serialize=false)
	@ManyToOne(fetch = FetchType.LAZY) 
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JSONField(serialize=false)
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "account", targetEntity = Privilege.class)
	public List<Privilege> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<Privilege> privilegeList) {
		this.privilegeList = privilegeList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "from", targetEntity = MessageInfo.class)
	public List<MessageInfo> getFormMessageList() {
		return formMessageList;
	}

	public void setFormMessageList(List<MessageInfo> formMessageList) {
		this.formMessageList = formMessageList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "to", targetEntity = MessageInfo.class)
	public List<MessageInfo> getToMessageList() {
		return toMessageList;
	}

	public void setToMessageList(List<MessageInfo> toMessageList) {
		this.toMessageList = toMessageList;
	}

	/**
	 * 密码盐.
	 * @return
	 */
	@Transient
    public String getCredentialsSalt(){
       return this.loginId+this.salt;
    }
	
	
	
}
