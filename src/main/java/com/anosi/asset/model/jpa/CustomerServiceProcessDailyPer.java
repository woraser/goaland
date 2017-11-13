package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customerServiceProcessDailyPer")
public class CustomerServiceProcessDailyPer extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6099195574734431565L;
	
	private Date countDate;
	
	private Long completed;
	
	private Long unCompleted;
	
	private String processDefKey;
	
	private List<CustomerServiceProcess> completedProcessList = new ArrayList<>();
	
	private List<CustomerServiceProcess> unCompletedProcessList = new ArrayList<>();

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}

	public Long getUnCompleted() {
		return unCompleted;
	}

	public void setUnCompleted(Long unCompleted) {
		this.unCompleted = unCompleted;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<CustomerServiceProcess> getCompletedProcessList() {
		return completedProcessList;
	}

	public void setCompletedProcessList(List<CustomerServiceProcess> completedProcessList) {
		this.completedProcessList = completedProcessList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<CustomerServiceProcess> getUnCompletedProcessList() {
		return unCompletedProcessList;
	}

	public void setUnCompletedProcessList(List<CustomerServiceProcess> unCompletedProcessList) {
		this.unCompletedProcessList = unCompletedProcessList;
	}
	
}
