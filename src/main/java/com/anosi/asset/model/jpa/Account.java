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

	@Field(analyze = Analyze.NO)
	private String emailAddress;
	
	@Field(analyze = Analyze.NO)
	private String tel;

	private boolean active = false;

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

	private List<StartDetail> startDetailList = new ArrayList<>();

	private List<ExamineDetail> examineDetailList = new ArrayList<>();

	private List<EvaluatingDetail> evaluatingDetailList = new ArrayList<>();

	private List<DistributeDetail> distributeDetailList = new ArrayList<>();

	private List<Device> reciveDeviceList = new ArrayList<>();
	
	private List<RepairDetail> entrusterList = new ArrayList<>();
	
	private List<RepairDetail> repairFellowList = new ArrayList<>();
	
	private List<EntrustDetail> entrustFellowList = new ArrayList<>();
	
	@ContainedIn
	private List<Device> ownerDeviceList = new ArrayList<>();

	@ContainedIn
	private List<Advertisement> advertisementList = new ArrayList<>();

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
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "nextAssignee")
	public List<StartDetail> getStartDetailList() {
		return startDetailList;
	}

	public void setStartDetailList(List<StartDetail> startDetailList) {
		this.startDetailList = startDetailList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "engineeDep")
	public List<ExamineDetail> getExamineDetailList() {
		return examineDetailList;
	}

	public void setExamineDetailList(List<ExamineDetail> examineDetailList) {
		this.examineDetailList = examineDetailList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "servicer")
	public List<EvaluatingDetail> getEvaluatingDetailList() {
		return evaluatingDetailList;
	}

	public void setEvaluatingDetailList(List<EvaluatingDetail> evaluatingDetailList) {
		this.evaluatingDetailList = evaluatingDetailList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "engineer")
	public List<DistributeDetail> getDistributeDetailList() {
		return distributeDetailList;
	}

	public void setDistributeDetailList(List<DistributeDetail> distributeDetailList) {
		this.distributeDetailList = distributeDetailList;
	}
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "entruster")
	public List<RepairDetail> getEntrusterList() {
		return entrusterList;
	}

	public void setEntrusterList(List<RepairDetail> entrusterList) {
		this.entrusterList = entrusterList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "fellowList")
	public List<RepairDetail> getRepairFellowList() {
		return repairFellowList;
	}

	public void setRepairFellowList(List<RepairDetail> repairFellowList) {
		this.repairFellowList = repairFellowList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "fellowList")
	public List<EntrustDetail> getEntrustFellowList() {
		return entrustFellowList;
	}

	public void setEntrustFellowList(List<EntrustDetail> entrustFellowList) {
		this.entrustFellowList = entrustFellowList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<RoleFunctionGroup> getRoleFunctionGroupList() {
		return roleFunctionGroupList;
	}

	public void setRoleFunctionGroupList(List<RoleFunctionGroup> roleFunctionGroupList) {
		this.roleFunctionGroupList = roleFunctionGroupList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "creater", targetEntity = Advertisement.class)
	public List<Advertisement> getAdvertisementList() {
		return advertisementList;
	}

	public void setAdvertisementList(List<Advertisement> advertisementList) {
		this.advertisementList = advertisementList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "remindReceiverList", targetEntity = Device.class)
	public List<Device> getReciveDeviceList() {
		return reciveDeviceList;
	}

	public void setReciveDeviceList(List<Device> reciveDeviceList) {
		this.reciveDeviceList = reciveDeviceList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "ownerList", targetEntity = Device.class)
	public List<Device> getOwnerDeviceList() {
		return ownerDeviceList;
	}

	public void setOwnerDeviceList(List<Device> ownerDeviceList) {
		this.ownerDeviceList = ownerDeviceList;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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
