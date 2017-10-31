package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.anosi.asset.model.elasticsearch.Content;

@Entity
@Table(name = "customerServiceProcess")
public class CustomerServiceProcess extends BaseProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;

	@Content(extractFields = { "applicant.name" })
	private Account applicant;// 发起人

	@Content(extractFields = { "project.name", "project.number", "project.location" })
	private Project project;// 涉及的项目

	private StartDetail startDetail;// 发起字段

	private ExamineDetail examineDetail = new ExamineDetail();// 领导审批字段

	private EvaluatingDetail evaluatingDetail;// 评估字段

	private DistributeDetail distributeDetail;// 派单字段

	private RepairDetail repairDetail;// 维修字段

	private AgreementStatus agreementStatus;// 合同状态

	private boolean file = false;// 是否有上传文件

	private Device device;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	@JoinColumn(nullable = false)
	public Account getApplicant() {
		return applicant;
	}

	public void setApplicant(Account applicant) {
		this.applicant = applicant;
	}

	public StartDetail getStartDetail() {
		return startDetail;
	}

	public void setStartDetail(StartDetail startDetail) {
		this.startDetail = startDetail;
	}

	public RepairDetail getRepairDetail() {
		return repairDetail;
	}

	public void setRepairDetail(RepairDetail repairDetail) {
		this.repairDetail = repairDetail;
	}

	public EvaluatingDetail getEvaluatingDetail() {
		return evaluatingDetail;
	}

	public void setEvaluatingDetail(EvaluatingDetail evaluatingDetail) {
		this.evaluatingDetail = evaluatingDetail;
	}

	public ExamineDetail getExamineDetail() {
		return examineDetail;
	}

	public void setExamineDetail(ExamineDetail examineDetail) {
		this.examineDetail = examineDetail;
	}

	public AgreementStatus getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(AgreementStatus agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	public DistributeDetail getDistributeDetail() {
		return distributeDetail;
	}

	public void setDistributeDetail(DistributeDetail distributeDetail) {
		this.distributeDetail = distributeDetail;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	/***
	 * 流程发起时的表单字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class StartDetail {

		private Belong belong;// 归属

		private String productName;// 产品名称

		private String productNo;// 产品编号

		private String productSpecifications;// 产品规格

		private ProductType productType;// 产品类型

		private String customerMan;// 客户联系人

		private String customerNumber;// 客户联系人电话

		private String projectMan;// 项目联系人

		private String projectNumber;// 项目联系人电话
		
		@JSONField(format="yyyy-MM-dd HH:mm:ss")
		private Date estimatedTime;// 预估维修时间

		private String baseDemands;// 基本要求

		private String specialDemands;// 特殊要求

		private String nextAssignee;// 下一步办理人

		public Belong getBelong() {
			return belong;
		}

		public void setBelong(Belong belong) {
			this.belong = belong;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getProductNo() {
			return productNo;
		}

		public void setProductNo(String productNo) {
			this.productNo = productNo;
		}

		public String getProductSpecifications() {
			return productSpecifications;
		}

		public void setProductSpecifications(String productSpecifications) {
			this.productSpecifications = productSpecifications;
		}

		public ProductType getProductType() {
			return productType;
		}

		public void setProductType(ProductType productType) {
			this.productType = productType;
		}

		public String getCustomerMan() {
			return customerMan;
		}

		public void setCustomerMan(String customerMan) {
			this.customerMan = customerMan;
		}

		public String getCustomerNumber() {
			return customerNumber;
		}

		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}

		public String getProjectMan() {
			return projectMan;
		}

		public void setProjectMan(String projectMan) {
			this.projectMan = projectMan;
		}

		public String getProjectNumber() {
			return projectNumber;
		}

		public void setProjectNumber(String projectNumber) {
			this.projectNumber = projectNumber;
		}

		public Date getEstimatedTime() {
			return estimatedTime;
		}

		public void setEstimatedTime(Date estimatedTime) {
			this.estimatedTime = estimatedTime;
		}

		public String getBaseDemands() {
			return baseDemands;
		}

		public void setBaseDemands(String baseDemands) {
			this.baseDemands = baseDemands;
		}

		public String getSpecialDemands() {
			return specialDemands;
		}

		public void setSpecialDemands(String specialDemands) {
			this.specialDemands = specialDemands;
		}

		public String getNextAssignee() {
			return nextAssignee;
		}

		public void setNextAssignee(String nextAssignee) {
			this.nextAssignee = nextAssignee;
		}

		public static enum ProductType {
			DC, FACTS, NEWENERGY, LABPROJECT, OTHER;
		}

		public static enum Belong {
			SOUTHERNPART, NORTHPART;
		}

	}

	/***
	 * 领导审批字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class ExamineDetail {

		private boolean reject = false;// 领导驳回

		private String suggestion;// 领导审批意见

		private String engineeDep;// 工程部评估人

		public boolean getReject() {
			return reject;
		}

		public void setReject(boolean reject) {
			this.reject = reject;
		}

		public String getSuggestion() {
			return suggestion;
		}

		public void setSuggestion(String suggestion) {
			this.suggestion = suggestion;
		}

		public String getEngineeDep() {
			return engineeDep;
		}

		public void setEngineeDep(String engineeDep) {
			this.engineeDep = engineeDep;
		}

	}

	/***
	 * 合同状态
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class AgreementStatus {

		private Date beginTime;

		private Date endTime;

		public static enum Agreement {
			UNDERGUARANTEE, BEYONDGUARANTEE;
		}

		public Date getBeginTime() {
			return beginTime;
		}

		public void setBeginTime(Date beginTime) {
			this.beginTime = beginTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		@Transient
		public Agreement getAgreement() {
			if (System.currentTimeMillis() < endTime.getTime()) {
				return Agreement.UNDERGUARANTEE;
			} else {
				return Agreement.BEYONDGUARANTEE;
			}
		}

	}

	/***
	 * 工程师评估字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class EvaluatingDetail {

		private String breakdownDevice;// 故障设备

		private String servicer;// 服务组人员

		public String getBreakdownDevice() {
			return breakdownDevice;
		}

		public void setBreakdownDevice(String breakdownDevice) {
			this.breakdownDevice = breakdownDevice;
		}

		public String getServicer() {
			return servicer;
		}

		public void setServicer(String servicer) {
			this.servicer = servicer;
		}

	}

	/***
	 * 派单字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class DistributeDetail {

		private String engineer;// 工程师

		public String getEngineer() {
			return engineer;
		}

		public void setEngineer(String engineer) {
			this.engineer = engineer;
		}

	}

	/***
	 * 维修的表单字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class RepairDetail {

		private String problemDescription;// 问题描述

		private String failureCause;// 故障原因

		private String processMode;// 处理方式

		public String getProblemDescription() {
			return problemDescription;
		}

		public void setProblemDescription(String problemDescription) {
			this.problemDescription = problemDescription;
		}

		public String getFailureCause() {
			return failureCause;
		}

		public void setFailureCause(String failureCause) {
			this.failureCause = failureCause;
		}

		public String getProcessMode() {
			return processMode;
		}

		public void setProcessMode(String processMode) {
			this.processMode = processMode;
		}

	}

}
