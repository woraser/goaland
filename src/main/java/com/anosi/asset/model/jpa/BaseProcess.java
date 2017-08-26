package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.anosi.asset.util.DateFormatUtil;

/***
 * 流程实体类的父类 属性中有流程相关的三个@Transient对象，需要每次使用的时候利用activiti的api
 * 
 * @author jinyao
 *
 */
@MappedSuperclass
public class BaseProcess extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6021851338293771084L;

	protected String processInstanceId;

	protected String name = DateFormatUtil.getFormateDate(new Date(), "yyyyMMddHHmmssSSS");

	protected Task task;

	protected HistoricTaskInstance historicTaskInstance;

	protected ProcessInstance processInstance;

	protected HistoricProcessInstance historicProcessInstance;
	
	protected FinishType finishType;
	
	public static enum FinishType{
		REAMIN("待办"),FINISHED("已完成"),DELETED("已删除"),REFUSED("被驳回"),FORCED("强制结束");
		
		private String name;
		
		private FinishType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

	@Column(unique = true, nullable = false)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FinishType getFinishType() {
		return finishType;
	}

	public void setFinishType(FinishType finishType) {
		this.finishType = finishType;
	}

	@Transient
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Transient
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	@Transient
	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	@Transient
	public HistoricTaskInstance getHistoricTaskInstance() {
		return historicTaskInstance;
	}

	public void setHistoricTaskInstance(HistoricTaskInstance historicTaskInstance) {
		this.historicTaskInstance = historicTaskInstance;
	}

}
