package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
		
		PASS("通过"),REFUSE("拒绝"),REAMIN_TO_DO("待办");
		
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

	public ProcessRecord setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public ProcessRecord setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public HandleType getType() {
		return type;
	}

	public ProcessRecord setType(HandleType type) {
		this.type = type;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public ProcessRecord setReason(String reason) {
		this.reason = reason;
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public ProcessRecord setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public ProcessRecord setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getTaskName() {
		return taskName;
	}

	public ProcessRecord setTaskName(String taskName) {
		this.taskName = taskName;
		return this;
	}
	
	public String getRemain() {
		return remain;
	}

	public ProcessRecord setRemain(String remain) {
		this.remain = remain;
		return this;
	}

	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Account.class)
	@JoinColumn(nullable=false)
	public Account getAssignee() {
		return assignee;
	}

	public ProcessRecord setAssignee(Account assignee) {
		this.assignee = assignee;
		return this;
	}
	
}
