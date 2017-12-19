package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class BaseRepairDetail extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1929311023615889909L;
	
	private Date repairStartTime;// 维修开始时间

	private Date repairEndTime;// 维修结束时间

	private String problemDescription;// 问题描述

	private String failureCause;// 故障原因

	private String processMode;// 处理方式
	
	@Type(type = "text")
	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	@Type(type = "text")
	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	@Type(type = "text")
	public String getProcessMode() {
		return processMode;
	}

	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}

	public Date getRepairStartTime() {
		return repairStartTime;
	}

	public void setRepairStartTime(Date repairStartTime) {
		this.repairStartTime = repairStartTime;
	}

	public Date getRepairEndTime() {
		return repairEndTime;
	}

	public void setRepairEndTime(Date repairEndTime) {
		this.repairEndTime = repairEndTime;
	}

}
