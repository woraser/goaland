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

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.annotation.JSONField;
import com.anosi.asset.model.elasticsearch.Content;

@Entity
@Table(name = "account")
@Indexed
@Analyzer(impl = IKAnalyzer.class)
/*
 * @NamedEntityGraphs({
 * 
 * @NamedEntityGraph(name = "account.department", attributeNodes
 * = @NamedAttributeNode(value = "roleList", subgraph = "depGroup"), //
 * 延伸roles中的depGroup subgraphs = {
 * 
 * @NamedSubgraph(name = "depGroup", attributeNodes = @NamedAttributeNode(value
 * = "depGroup", subgraph = "department")), // 再延伸depGroup的department
 * 
 * @NamedSubgraph(name = "department", attributeNodes
 * = @NamedAttributeNode(value = "department")) }), })
 */
public class Account extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1503690620448714787L;

	@Content
	@Field
	private String name;

	@Content
	@Field(analyze = Analyze.NO)
	private String loginId;

	private String password;

	@Content(extractFields = { "roleList*.name", "roleList*.depGroup.name", "roleList*.depGroup.department.name" })
	@IndexedEmbedded
	private List<Role> roleList = new ArrayList<>();

	private boolean uploadDocument = false;// 是否上传过技术文档

	private List<Privilege> privilegeList = new ArrayList<>();

	@Content(extractFields = { "roleFunctionGroupList*.name" })
	private List<RoleFunctionGroup> roleFunctionGroupList = new ArrayList<>();

	private List<MessageInfo> formMessageList = new ArrayList<>();

	private List<MessageInfo> toMessageList = new ArrayList<>();

	private List<ProcessRecord> processRecordList = new ArrayList<>();

	@ContainedIn
	private List<CustomerServiceProcess> customerServiceProcesseList = new ArrayList<>();
	
	private List<CustomerServiceProcess> nextAssigneeProcesseList = new ArrayList<>();
	
	private List<CustomerServiceProcess> engineeDepProcesseList = new ArrayList<>();
	
	private List<CustomerServiceProcess> servicerProcesseList = new ArrayList<>();
	
	private List<CustomerServiceProcess> engineerProcesseList = new ArrayList<>();
	
	private List<CustomerServiceProcess> repairerProcesseList = new ArrayList<>();

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

	@Column(unique = true, nullable = false)
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
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "nextAssignee", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getNextAssigneeProcesseList() {
		return nextAssigneeProcesseList;
	}

	public void setNextAssigneeProcesseList(List<CustomerServiceProcess> nextAssigneeProcesseList) {
		this.nextAssigneeProcesseList = nextAssigneeProcesseList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "engineeDep", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getEngineeDepProcesseList() {
		return engineeDepProcesseList;
	}

	public void setEngineeDepProcesseList(List<CustomerServiceProcess> engineeDepProcesseList) {
		this.engineeDepProcesseList = engineeDepProcesseList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "servicer", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getServicerProcesseList() {
		return servicerProcesseList;
	}

	public void setServicerProcesseList(List<CustomerServiceProcess> servicerProcesseList) {
		this.servicerProcesseList = servicerProcesseList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "engineer", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getEngineerProcesseList() {
		return engineerProcesseList;
	}

	public void setEngineerProcesseList(List<CustomerServiceProcess> engineerProcesseList) {
		this.engineerProcesseList = engineerProcesseList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "repairer", targetEntity = CustomerServiceProcess.class)
	public List<CustomerServiceProcess> getRepairerProcesseList() {
		return repairerProcesseList;
	}

	public void setRepairerProcesseList(List<CustomerServiceProcess> repairerProcesseList) {
		this.repairerProcesseList = repairerProcesseList;
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
	 * 
	 * @return
	 */
	@Transient
	public Department getDepartment() {
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
