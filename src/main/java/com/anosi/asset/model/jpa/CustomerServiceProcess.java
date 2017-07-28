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
	
	private Account applicant;
	
	private LaunchDetail launchDetail;
	
	private RepairDetail repairDetail;
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	@JoinColumn(nullable=false)
	public Account getApplicant() {
		return applicant;
	}

	public void setApplicant(Account applicant) {
		this.applicant = applicant;
	}

	public LaunchDetail getLaunchDetail() {
		return launchDetail;
	}

	public void setLaunchDetail(LaunchDetail launchDetail) {
		this.launchDetail = launchDetail;
	}
	
	public RepairDetail getRepairDetail() {
		return repairDetail;
	}

	public void setRepairDetail(RepairDetail repairDetail) {
		this.repairDetail = repairDetail;
	}

	/***
	 * 内部类定义组件
	 * @author jinyao
	 *
	 */
	@Embeddable
	class LaunchDetail{
		
		private String sceneDescription;

		public String getSceneDescription() {
			return sceneDescription;
		}

		public void setSceneDescription(String sceneDescription) {
			this.sceneDescription = sceneDescription;
		}
		
	}
	
	@Embeddable
	class RepairDetail{
		
	}

}
