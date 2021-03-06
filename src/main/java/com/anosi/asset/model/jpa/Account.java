package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.anosi.asset.model.elasticsearch.Content;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1503690620448714787L;

	@Content
	private String name;

	@Content
	private String loginId;

	private String password;

	@Content(extractFields = { "roleList*.name","roleList*.depGroup.name","roleList*.depGroup.department.name" })
	private List<Role> roleList = new ArrayList<>();

	private boolean uploadDocument = false;// 是否上传过文件

	private List<Privilege> privilegeList = new ArrayList<>();

	@Content(extractFields = { "roleFunctionGroupList*.name" })
	private List<RoleFunctionGroup> roleFunctionGroupList = new ArrayList<>();

	private List<MessageInfo> formMessageList = new ArrayList<>();

	private List<MessageInfo> toMessageList = new ArrayList<>();

	private List<ProcessRecord> processRecordList = new ArrayList<>();

	private List<CustomerServiceProcess> customerServiceProcesseList = new ArrayList<>();

	// 密码加盐
	private String salt;

	@Column(unique = true, nullable = false)
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

	@JSONField(serialize = false)
	@ManyToMany(fetch = FetchType.LAZY)
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JSONField(serialize = false)
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

	public boolean isUploadDocument() {
		return uploadDocument;
	}

	public void setUploadDocument(boolean uploadDocument) {
		this.uploadDocument = uploadDocument;
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

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "assignee", targetEntity = ProcessRecord.class)
	public List<ProcessRecord> getProcessRecordList() {
		return processRecordList;
	}

	public void setProcessRecordList(List<ProcessRecord> processRecordList) {
		this.processRecordList = processRecordList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "applicant", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getCustomerServiceProcesseList() {
		return customerServiceProcesseList;
	}

	public void setCustomerServiceProcesseList(List<CustomerServiceProcess> customerServiceProcesseList) {
		this.customerServiceProcesseList = customerServiceProcesseList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<RoleFunctionGroup> getRoleFunctionGroupList() {
		return roleFunctionGroupList;
	}

	public void setRoleFunctionGroupList(List<RoleFunctionGroup> roleFunctionGroupList) {
		this.roleFunctionGroupList = roleFunctionGroupList;
	}
	
	/***
	 * 获取部门
	 * @return
	 */
	@Transient
	public Department getDepartment(){
		return roleList.get(0).getDepGroup().getDepartment();
	}

	/**
	 * 密码盐.
	 * 
	 * @return
	 */
	@Transient
	public String getCredentialsSalt() {
		return this.loginId + this.salt;
	}

}
