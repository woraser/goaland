package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/***
 * 设备种类的结构
 * @author jinyao
 *
 */
@Entity
@Table(name = "devCategoryStructures")
public class DevCategoryStructures extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5669581495003987099L;

	private DevCategory mainDevCategory;
	
	private DevCategory subDevCategory;
	
	private Integer amount;

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=DevCategory.class)
	public DevCategory getMainDevCategory() {
		return mainDevCategory;
	}

	public void setMainDevCategory(DevCategory mainDevCategory) {
		this.mainDevCategory = mainDevCategory;
	}
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=DevCategory.class)
	public DevCategory getSubDevCategory() {
		return subDevCategory;
	}

	public void setSubDevCategory(DevCategory subDevCategory) {
		this.subDevCategory = subDevCategory;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
