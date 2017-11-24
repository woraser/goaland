package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "processRecord")
public class ProcessRecord extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4227754366837760082L;
	
	private String processInstanceId;
	
	private HandleType type;
	
	private String reason;
	
	private Date startTime;
	
	private Date endTime;
	
	private String taskName;
	
	private String taskId;
	
	private String remain;//待办人
	
	private Account assignee;//实际办理人
	
	/***
	 * 对任务的处理方式
	 * @author jinyao
	 *
	 */
	public static enum HandleType{
		
		PASS("通过"),REFUSE("拒绝"),REMAIN_TO_DO("待办"),ENTRUST("委托");
		
		private String type;

		private HandleType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public HandleType getType() {
		return type;
	}

	public void setType(HandleType type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	public Account getAssignee() {
		return assignee;
	}

	public void setAssignee(Account assignee) {
		this.assignee = assignee;
	}
	
}
