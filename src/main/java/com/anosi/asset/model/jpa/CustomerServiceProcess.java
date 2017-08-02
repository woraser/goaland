package com.anosi.asset.model.jpa;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="customerServiceProcess")
public class CustomerServiceProcess extends BaseProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;
	
	private Account applicant;//发起人
	
	private StartDetail startDetail;//发起字段
	
	private EvaluatingDetail evaluatingDetail;//评估字段
	
	private RepairDetail repairDetail;//维修字段
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	@JoinColumn(nullable=false)
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


	/***
	 * 流程发起时的表单字段
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class StartDetail{
		
		private String projectNo;//项目编号
		
		private String projectName;//项目名称
		
		private String projectLocation;//项目地址
		
		private String contactMan;//联系人
		
		private String contactNumber;//联系电话
		
		private String proposeTime;//提出时间
		
		private String sceneDescription;//现场描述
		
		private String estimateDevices;//预估维修设备
		
		private String otherDemands;//其他要求
		
		public String getProjectNo() {
			return projectNo;
		}

		public void setProjectNo(String projectNo) {
			this.projectNo = projectNo;
		}

		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public String getProjectLocation() {
			return projectLocation;
		}

		public void setProjectLocation(String projectLocation) {
			this.projectLocation = projectLocation;
		}

		public String getContactMan() {
			return contactMan;
		}

		public void setContactMan(String contactMan) {
			this.contactMan = contactMan;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getProposeTime() {
			return proposeTime;
		}

		public void setProposeTime(String proposeTime) {
			this.proposeTime = proposeTime;
		}

		public String getEstimateDevices() {
			return estimateDevices;
		}

		public void setEstimateDevices(String estimateDevices) {
			this.estimateDevices = estimateDevices;
		}

		public String getOtherDemands() {
			return otherDemands;
		}

		public void setOtherDemands(String otherDemands) {
			this.otherDemands = otherDemands;
		}

		public String getSceneDescription() {
			return sceneDescription;
		}

		public void setSceneDescription(String sceneDescription) {
			this.sceneDescription = sceneDescription;
		}
		
	}
	
	/***
	 * 工程师评估字段
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class EvaluatingDetail{
		
		private String breakdownDevice;//故障设备

		public String getBreakdownDevice() {
			return breakdownDevice;
		}

		public void setBreakdownDevice(String breakdownDevice) {
			this.breakdownDevice = breakdownDevice;
		}
		
	}
	
	/***
	 * 维修的表单字段
	 * @author jinyao
	 *
	 */
	@Embeddable
	public static class RepairDetail{
		
		private String problemDescription;//问题描述
		
		private String failureCause;//故障原因
		
		private String processMode;//处理方式

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
