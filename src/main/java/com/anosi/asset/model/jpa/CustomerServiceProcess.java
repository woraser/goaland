package com.anosi.asset.model.jpa;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customerServiceProcess")
public class CustomerServiceProcess extends BaseProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;
	
	private LaunchDetail launchDetail;
	
	public LaunchDetail getLaunchDetail() {
		return launchDetail;
	}

	public void setLaunchDetail(LaunchDetail launchDetail) {
		this.launchDetail = launchDetail;
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
