package com.anosi.asset.model.jpa;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.anosi.asset.model.elasticsearch.Content;

@Entity
@Table(name = "customerServiceProcess")
public class CustomerServiceProcess extends BaseProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;

	private Account applicant;// 发起人

	@Content(extractFields = { "project.name", "project.number", "project.location" })
	private Project project;// 涉及的项目

	private StartDetail startDetail;// 发起字段

	private ExamineDetail examineDetail;// 领导审批字段

	private EvaluatingDetail evaluatingDetail;// 评估字段

	private RepairDetail repairDetail;// 维修字段

	private boolean file = false;// 是否有上传文件

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

	/***
	 * 流程发起时的表单字段
	 * 
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class StartDetail {

		private String belong;// 归属

		private String productName;// 产品名称

		private String productNo;// 产品编号

		private String productSpecifications;// 产品规格
		
		private ProductType productType;//产品类型

		private String customerMan;// 客户联系人

		private String customerNumber;// 客户联系人电话

		private String projectMan;// 项目联系人

		private String projectNumber;// 项目联系人电话

		private String estimatedTime;// 预估维修时间

		private String baseDemands;// 基本要求

		private String specialDemands;// 特殊要求
		
		public String getBelong() {
			return belong;
		}

		public void setBelong(String belong) {
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

		public String getEstimatedTime() {
			return estimatedTime;
		}

		public void setEstimatedTime(String estimatedTime) {
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

		public static enum ProductType {

			DC("直流类"), FACTS("FACTS(含SVC,TCSC,STATCOM)"), NEWENERGY("新能源类(含风电,光伏,高压变频)"), LABPROJECT("实验室项目"), OTHER(
					"其他");

			private String name;

			private ProductType(String name) {
				this.name = name;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

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

		private String suggestion;// 领导审批意见

		public String getSuggestion() {
			return suggestion;
		}

		public void setSuggestion(String suggestion) {
			this.suggestion = suggestion;
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

		public String getBreakdownDevice() {
			return breakdownDevice;
		}

		public void setBreakdownDevice(String breakdownDevice) {
			this.breakdownDevice = breakdownDevice;
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
