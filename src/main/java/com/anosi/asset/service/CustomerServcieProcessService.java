package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServcieProcessService extends BaseProcessService<CustomerServiceProcess>{

	/***
	 * 销售部/工程部/质检部发起
	 * 流程变量:${assignee}:发起人
	 */
	void startProcess();
	
	/***
	 * 发起人完成发起的清单
	 * 流程变量:${engineeDep}:下一步工程部办理人
	 */
	void completeProcess(Account engineeDep,String taskId);
	
	/***
	 * 工程部问题评估
	 * 流程变量:${servicer}:下一步售后服务组办理人
	 */
	void evaluating(Account servicer,String taskId);
	
	/***
	 * 售后服务组派单
	 * 流程变量:${engineer}:下一步工程师
	 */
	void distribute(Account engineer,String taskId);
	
	/***
	 * 工程师上门维修
	 */
	void repair(String taskId);
	
}
