package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 故障分类
 * @author jinyao
 *
 */
@Entity
@Table(name = "faultCategory")
public class FaultCategory extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201081115327257218L;

	private String name;
	
	@JSONField(serialize=false)
	private List<RepairDetail> repairDetailList = new ArrayList<>();
	
	@JSONField(serialize=false)
	private List<EntrustDetail> entrustDetailList = new ArrayList<>();

	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "faultCategoryList")
	public List<RepairDetail> getRepairDetailList() {
		return repairDetailList;
	}

	public void setRepairDetailList(List<RepairDetail> repairDetailList) {
		this.repairDetailList = repairDetailList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "faultCategoryList")
	public List<EntrustDetail> getEntrustDetailList() {
		return entrustDetailList;
	}

	public void setEntrustDetailList(List<EntrustDetail> entrustDetailList) {
		this.entrustDetailList = entrustDetailList;
	}
	
}
