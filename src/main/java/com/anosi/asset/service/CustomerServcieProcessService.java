package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServcieProcessService extends BaseProcessService<CustomerServiceProcess>{

	/***
	 * 销售部/工程部/质检部发起
	 * 流程变量:${assignee}:发起人
	 */
	void startProcess();
	
	/**
	 * 发起人完成发起的清单
	 * @param engineeDep 流程变量,下一步工程部办理人${engineeDep}
	 * @param taskId
	 */
	void completeProcess(Account engineeDep,String taskId);
	
	/***
	 * 工程部问题评估
	 * @param servicer 流程变量:${servicer}:下一步售后服务组办理人
	 * @param taskId
	 */
	void evaluating(Account servicer,String taskId);
	
	/***
	 * 售后服务组派单
	 * @param engineer 流程变量:${engineer}:下一步工程师
	 * @param taskId
	 */
	void distribute(Account engineer,String taskId);
	
	/***
	 * 工程师上门维修
	 * @param taskId
	 */
	void repair(String taskId);
	
	/***
	 * 委托
	 * @param taskId
	 * @param mandatary
	 */
	void entrust(String taskId,Account mandatary);
	
}
